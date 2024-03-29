package com.ninekicks.microservices.service

import org.springframework.http.ResponseEntity

interface ProductDetailService {
    fun fetchProductDetail(productId:String): ResponseEntity<Any>

}