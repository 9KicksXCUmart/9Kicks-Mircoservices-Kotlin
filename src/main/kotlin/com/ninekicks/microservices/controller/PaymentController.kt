package com.ninekicks.microservices.controller

import com.ninekicks.microservices.model.CardToken
import com.ninekicks.microservices.service.impl.PaymentServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/payment")
class PaymentController(
    private val paymentService: PaymentServiceImpl
) {
    @PostMapping("/create-payment-intent")
    fun createPaymentIntent(@RequestBody payload:String): ResponseEntity<Any> {
        return paymentService.createPaymentIntent()
    }

}