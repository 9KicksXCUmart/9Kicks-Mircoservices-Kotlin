package com.ninekicks.microservices.service

import com.ninekicks.microservices.model.dto.SummarizePaymentDTO
import com.stripe.model.PaymentIntent
import org.springframework.http.ResponseEntity

interface PaymentService {
    fun summarizePayment(payload: SummarizePaymentDTO): ResponseEntity<Any>
    fun summarizePaymentMethod(paymentIntent: PaymentIntent):Map<String,Any>
}