package com.ninekicks.microservices.controller

import com.ninekicks.microservices.model.dto.OrderCreateDTO
import com.ninekicks.microservices.model.dto.OrderDetailDTO
import com.ninekicks.microservices.service.impl.GetAuthenticationServiceImpl
import com.ninekicks.microservices.service.impl.PaymentServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/payment")
class PaymentController(
    private val paymentService: PaymentServiceImpl,
    private val authenticationService: GetAuthenticationServiceImpl
) {
    @GetMapping("/summarize-payment")
    fun summarizePayment(@RequestParam("token") token:String): ResponseEntity<Any> {
        return paymentService.summarizePayment(token,authenticationService.getUserId())
    }

    @GetMapping("/order-summary")
    fun getOrderSummary(): ResponseEntity<Any> {
        println(authenticationService.getUserId())
        return paymentService.getOrderSummary(authenticationService.getUserId())
    }

    @PostMapping("/create-order-record")
    fun createOrderRecord(@RequestBody orderDetail: OrderCreateDTO): ResponseEntity<Any> {
        return paymentService.createOrderRecord(orderDetail,authenticationService.getUserId() )
    }
}