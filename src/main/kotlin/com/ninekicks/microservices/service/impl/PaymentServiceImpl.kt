package com.ninekicks.microservices.service.impl

import com.ninekicks.microservices.config.ResponseHandler
import com.ninekicks.microservices.model.dto.SummarizePaymentDTO
import com.ninekicks.microservices.service.PaymentService
import com.stripe.Stripe
import com.stripe.model.PaymentIntent
import com.stripe.model.PaymentMethod
import com.stripe.param.PaymentIntentCreateParams
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service


@Service
class PaymentServiceImpl(
    @Value("\${api.stripe.key}")
    private val STRIPEPK:String
):PaymentService{
    @PostConstruct
    fun init() {
        Stripe.apiKey = STRIPEPK
    }
    private val responseHandler = ResponseHandler()

    override fun summarizePayment(payload: SummarizePaymentDTO): ResponseEntity<Any> {
        var paymentIntent: PaymentIntent?=null
        var detail:Any?=null
        try{
         val params = PaymentIntentCreateParams.builder()
             .setAmount(payload.totalPrice*100)
             .setCurrency("usd")
             .setPaymentMethod(payload.paymentMethodId)
             .build()
             paymentIntent= PaymentIntent.create(params)
            detail = summarizePaymentMethod(paymentIntent)
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

    override fun summarizePaymentMethod(paymentIntent: PaymentIntent): Map<String, Any> {
        var paymentMethodDetail = PaymentMethod.retrieve(paymentIntent.paymentMethod)
        var orderConfirmDetail:MutableMap<String,Any> = HashMap()
        orderConfirmDetail.put("brand",paymentMethodDetail.card.brand)
        orderConfirmDetail.put("exp_month",paymentMethodDetail.card.expMonth)
        orderConfirmDetail.put("exp_year",paymentMethodDetail.card.expYear)
        orderConfirmDetail.put("last4",paymentMethodDetail.card.last4)
        orderConfirmDetail.put("clientSecret",paymentIntent.clientSecret)
        return orderConfirmDetail.toMap()
    }


}