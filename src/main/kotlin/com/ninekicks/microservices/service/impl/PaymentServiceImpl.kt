package com.ninekicks.microservices.service.impl

import com.ninekicks.microservices.config.ResponseHandler
import com.ninekicks.microservices.model.ShoppingCart
import com.ninekicks.microservices.model.dto.OrderCreateDTO
import com.ninekicks.microservices.model.dto.ProductDiscountPriceDTO
import com.ninekicks.microservices.model.dto.ProductPriceDetailDTO
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
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service


@Service
class PaymentServiceImpl(
    @Value("\${api.stripe.key}")
    private val STRIPEPK:String,
    private val userRepository: UserRepositoryImpl,
    private val orderRepository: OrderRepositoryImpl,
    private val productDetailService: ProductDetailServiceImpl,
    private val shoppingCartService: ShoppingCartServiceImpl
):PaymentService{
    @PostConstruct
    fun init() {
        Stripe.apiKey = STRIPEPK
    }
    private val responseHandler = ResponseHandler()

    override fun summarizePayment(confirmTokenId:String,userId: String): ResponseEntity<Any> {
        var detail:Any?=null
        var priceDetail:ProductPriceDetailDTO?=null
        var charge:Long = 0
        try{
            val confirmationToken: ConfirmationToken = ConfirmationToken.retrieve(confirmTokenId)
            detail = summarizeConfirmationToken(confirmationToken)
            priceDetail = getPriceDetail(userId)
            if(confirmationToken.paymentMethodPreview.billingDetails.address.line2=="Express Delivery")
                priceDetail!!.actualPrice *= 100
                charge = priceDetail!!.actualPrice.toLong()+1500
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

    override fun getPriceDetail(userId: String): ProductPriceDetailDTO? {
        var shoppingCartDetail:ShoppingCart?
        var totalPrice: Double = 0.0
        var actualPrice: Double = 0.0
        var discount: Double = 0.0
        var itemCount: Int = 0
        try{
            runBlocking {
                shoppingCartDetail = userRepository.getShoppingCartDeatil(userId)
            }
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

    override fun createOrderRecord(orderDetail: OrderCreateDTO,userId: String): ResponseEntity<Any> {
        return runBlocking {
            val order = orderRepository.createOrder(orderDetail,userId)
            shoppingCartService.clearShoppingCartItems(userId)
            responseHandler.validateResponse(
                failMessage = "Unable to Create Order",
                matchingObject = order,
                failReturnObject = null
            )
        }
    }
}
