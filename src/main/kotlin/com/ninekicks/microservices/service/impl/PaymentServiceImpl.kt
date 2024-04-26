package com.ninekicks.microservices.service.impl

import com.ninekicks.microservices.config.AppConfig
import com.ninekicks.microservices.config.ResponseHandler
import com.ninekicks.microservices.model.Order
import com.ninekicks.microservices.model.ShoppingCart
import com.ninekicks.microservices.model.dto.OrderCreateDTO
import com.ninekicks.microservices.model.dto.ProductDiscountPriceDTO
import com.ninekicks.microservices.model.dto.ProductPriceDetailDTO
import com.ninekicks.microservices.model.dto.StockUpdateResponseDTO
import com.ninekicks.microservices.repository.impl.OrderRepositoryImpl
import com.ninekicks.microservices.repository.impl.UserRepositoryImpl
import com.ninekicks.microservices.service.PaymentService
import com.stripe.Stripe
import com.stripe.model.ConfirmationToken
import com.stripe.model.PaymentIntent
import com.stripe.param.PaymentIntentCreateParams
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate


@Service
class PaymentServiceImpl(
    @Value("\${api.stripe.key}")
    private val STRIPEPK:String,
    private val userRepository: UserRepositoryImpl,
    private val orderRepository: OrderRepositoryImpl,
    private val productDetailService: ProductDetailServiceImpl,
    private val shoppingCartService: ShoppingCartServiceImpl,
    private val appConfig: AppConfig
):PaymentService{
    // load stripe private key
    @PostConstruct
    fun init() {
        Stripe.apiKey = STRIPEPK
    }
    private val responseHandler = ResponseHandler()
    // Frontend will call stripe API to post the payment information and create a confirmation token
    // Using confirmationId to retrieve payment info from Stripe and return to Frontend for two-step confirmation
    override fun summarizePayment(confirmTokenId:String,userId: String): ResponseEntity<Any> {
        var detail:Any?=null
        var priceDetail:ProductPriceDetailDTO?=null
        var charge:Long = 0
        try{
            val confirmationToken: ConfirmationToken = ConfirmationToken.retrieve(confirmTokenId)
            // Retrieve the payment information from ConfirmationToken Object and create detail Map Object
            detail = summarizeConfirmationToken(confirmationToken)
            priceDetail = getPriceDetail(userId)
            if(confirmationToken.paymentMethodPreview.billingDetails.address.line2=="Express Delivery")
                priceDetail!!.actualPrice *= 100
                charge = priceDetail!!.actualPrice.toLong()+1500
            //build a paymentIntent session and add client_secret to detail Map Object
            val params = PaymentIntentCreateParams
                .builder()
                .setAmount(charge!!)
                .setCurrency("usd")
                .setAutomaticPaymentMethods(
                    PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build()
                )
                .build()
            val paymentIntent = PaymentIntent.create(params)
            detail["client_secret"] = paymentIntent.clientSecret
        }catch(e: Exception){
            println(e.message)
        }finally {
            return responseHandler.validateResponse(
                failMessage = "Fail to create paymentIntent",
                matchingObject = detail,
                failReturnObject = null
            )
        }
     }
    // Convert ConfirmationToken Object into Detail Map Object and return it
    override fun summarizeConfirmationToken(confirmationTokenDetail: ConfirmationToken): MutableMap<String, Any> {
        var orderConfirmDetail:MutableMap<String,Any> = HashMap()
        orderConfirmDetail.put("name",confirmationTokenDetail.paymentMethodPreview.billingDetails.name)
        orderConfirmDetail.put("email",confirmationTokenDetail.paymentMethodPreview.billingDetails.email)
        orderConfirmDetail.put("last4",confirmationTokenDetail.paymentMethodPreview.card.last4)
        orderConfirmDetail.put("brand",confirmationTokenDetail.paymentMethodPreview.card.brand)
        orderConfirmDetail.put("line1",confirmationTokenDetail.paymentMethodPreview.billingDetails.address.line1)
        orderConfirmDetail.put("line2",confirmationTokenDetail.paymentMethodPreview.billingDetails.address.line2)
        orderConfirmDetail.put("state",confirmationTokenDetail.paymentMethodPreview.billingDetails.address.state)
        orderConfirmDetail.put("country","Hong Kong SAR, China")
        return orderConfirmDetail
    }
    // Get total price, total discount, discount, price for Shopping Cart Items
    override fun getOrderSummary(userId: String):  ResponseEntity<Any> {
        var productPriceDetail:ProductPriceDetailDTO?=null
        try{
            productPriceDetail = getPriceDetail(userId)
        }catch (e:Exception){
            println(e)
        }finally {
            return responseHandler.validateResponse(
                failMessage = "Fail to get order summary",
                matchingObject = productPriceDetail,
                failReturnObject = null

            )
        }
    }
    // get price detail for Order Summary
    override fun getPriceDetail(userId: String): ProductPriceDetailDTO? {
        // Get Shopping Cart Items to retrieve the price and discount of each Shopping Cart Items
        var shoppingCartDetail:ShoppingCart?
        var totalPrice: Double = 0.0
        var actualPrice: Double = 0.0
        var discount: Double = 0.0
        var itemCount: Int = 0
        try{
            runBlocking {
                shoppingCartDetail = userRepository.getShoppingCartDetail(userId)
            }
            // Sum the price and discount up to get the total discount and discount price
            shoppingCartDetail?.shoppingCartItemDetail?.forEach {
                itemCount += it.value.productQuantity
                var product:ProductDiscountPriceDTO? = productDetailService.fetchProductDiscountedPrice(it.value.productId)
                totalPrice += (product!!.price*it.value.productQuantity)
                if(product!!.isDiscounted){
                    actualPrice += (product.discountPrice!!*it.value.productQuantity)
                    discount += ((product.price - product.discountPrice!!)*it.value.productQuantity)
                }
                else
                    actualPrice += product.price*it.value.productQuantity
            }
            return ProductPriceDetailDTO(
                totalPrice=totalPrice,
                actualPrice=actualPrice,
                itemCount=itemCount,
                discount = discount,
                shippingFee = 0.0
            )
        }catch (e:Exception){
            println(e)
            return null
        }
    }
    // Create order roecord and insert to DynamoDB
    override fun createOrderRecord(orderDetail: OrderCreateDTO,userId: String): ResponseEntity<Any> {
        return runBlocking {
            val order = orderRepository.createOrder(orderDetail,userId)
            shoppingCartService.clearShoppingCartItems(userId)
            updateStock(orderDetail.orderItemDetail)
            responseHandler.validateResponse(
                failMessage = "Unable to Create Order",
                matchingObject = order,
                failReturnObject = null
            )
        }
    }
    // After customer finish payment process, update stock count base on what customer had bought
    override fun updateStock(list:List<Order.OrderItemDetail>?): Boolean {
        return try{
            list!!.forEach {
                // Post productId and productSize to other mirco-service to update stock quantity
                val restTemplate = RestTemplate()
                val headers = HttpHeaders()
                val entity = HttpEntity<String>("", headers)
                restTemplate.exchange(
                    "${appConfig.goBackendUrl}/v1/products/${it.productId}?size=${it.productSize}&sold=${it.productQuantity}",
                    HttpMethod.POST,
                    entity,
                    StockUpdateResponseDTO::class.java
                )
            }
            true
        }catch (e:Exception){
            println(e)
            false
        }
    }
}
