package com.ninekicks.microservices.model.dto



data class ProductDiscountPriceDTO(
    val price: Double,
    val isDiscounted: Boolean,
    val discountPrice: Double? = null
){
}