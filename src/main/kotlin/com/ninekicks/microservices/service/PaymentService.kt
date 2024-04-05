package com.ninekicks.microservices.service

import com.ninekicks.microservices.model.dto.OrderCreateDTO
import com.ninekicks.microservices.model.dto.OrderDetailDTO
import com.ninekicks.microservices.model.dto.ProductPriceDetailDTO
import com.stripe.model.ConfirmationToken
import org.springframework.http.ResponseEntity

interface PaymentService {
    fun summarizePayment(confirmTokenId:String, userId: String): ResponseEntity<Any>
    fun summarizeConfirmationToken(confirmationTokenDetail: ConfirmationToken):Map<String,Any>
    fun getOrderSummary(userId:String): ResponseEntity<Any>
    fun getPriceDetail(userId:String): ProductPriceDetailDTO?
    fun createOrderRecord(orderDetail:OrderCreateDTO):ResponseEntity<Any>
}