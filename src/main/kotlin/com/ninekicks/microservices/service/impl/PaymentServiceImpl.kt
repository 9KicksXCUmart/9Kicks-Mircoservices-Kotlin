package com.ninekicks.microservices.service.impl

import com.ninekicks.microservices.config.ResponseHandler
import com.ninekicks.microservices.model.CardToken
import com.ninekicks.microservices.service.PaymentService
import com.stripe.Stripe
import com.stripe.model.PaymentIntent
import com.stripe.model.Token
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

    override fun createPaymentIntent(): ResponseEntity<Any> {
         var paymentIntent: PaymentIntent?=null
        try{
         val params = PaymentIntentCreateParams.builder()
             .setAmount(500L*100)
             .setCurrency("usd")
             .addPaymentMethodType("card")
             .build()
             paymentIntent= PaymentIntent.create(params)
        }catch(e: Exception){
            println(e.message)
        }finally {
            return responseHandler.validateResponse(
                failMessage = "Fail to create paymentIntent",
                matchingObject = mapOf("clientSecret" to paymentIntent?.clientSecret),
                failObject = null
            )
        }
     }




}