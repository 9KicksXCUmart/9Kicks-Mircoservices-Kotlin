package com.ninekicks.microservices.model.dto

data class ProductPriceDetailDTO(
    var totalPrice: Double,
    var actualPrice: Double,
    var discount: Double,
    var itemCount: Int,
    var shippingFee: Double,
) {
}