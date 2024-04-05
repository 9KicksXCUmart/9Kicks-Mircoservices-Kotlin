package com.ninekicks.microservices.service

import com.ninekicks.microservices.model.dto.ProductDiscountPriceDTO
import org.springframework.http.ResponseEntity

interface ProductDetailService {
    fun fetchProductDetail(productId:String): ResponseEntity<Any>
    fun fetchProductDiscountedPrice(productId: String): ProductDiscountPriceDTO?

}