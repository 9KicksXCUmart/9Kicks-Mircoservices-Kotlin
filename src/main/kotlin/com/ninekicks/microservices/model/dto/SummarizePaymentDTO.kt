package com.ninekicks.microservices.model.dto

data class SummarizePaymentDTO(
    val paymentMethodId: String,
    val totalPrice: Long,
    ) {
}