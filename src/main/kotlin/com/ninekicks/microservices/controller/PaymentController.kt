package com.ninekicks.microservices.controller

import com.ninekicks.microservices.model.dto.OrderCreateDTO
import com.ninekicks.microservices.model.dto.OrderDetailDTO
import com.ninekicks.microservices.service.impl.PaymentServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/payment")
class PaymentController(
    private val paymentService: PaymentServiceImpl
) {
    @GetMapping("/summarize-payment")
    fun summarizePayment(@RequestParam("token") token:String, @RequestParam("userId") userId:String): ResponseEntity<Any> {
        return paymentService.summarizePayment(token,userId)
    }

    @GetMapping("/order-summary")
    fun getOrderSummary(@RequestParam("userId") userId:String): ResponseEntity<Any> {
        return paymentService.getOrderSummary(userId)
    }

    @PostMapping("/create-order-record")
    fun createOrderRecord(@RequestBody orderDetail: OrderCreateDTO): ResponseEntity<Any> {
        return paymentService.createOrderRecord(orderDetail)
    }
}