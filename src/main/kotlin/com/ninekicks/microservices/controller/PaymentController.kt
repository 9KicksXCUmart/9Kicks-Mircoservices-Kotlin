package com.ninekicks.microservices.controller

import com.ninekicks.microservices.model.dto.SummarizePaymentDTO
import com.ninekicks.microservices.service.impl.PaymentServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/payment")
class PaymentController(
    private val paymentService: PaymentServiceImpl
) {
    @PostMapping("/summarize-payment")
    fun summarizePayment(@RequestBody payload: SummarizePaymentDTO): ResponseEntity<Any> {
        return paymentService.summarizePayment(payload)
    }


}