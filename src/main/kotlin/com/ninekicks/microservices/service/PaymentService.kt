package com.ninekicks.microservices.service

import com.ninekicks.microservices.model.CardToken
import org.springframework.http.ResponseEntity

interface PaymentService {
    fun createPaymentIntent(): ResponseEntity<Any>
}