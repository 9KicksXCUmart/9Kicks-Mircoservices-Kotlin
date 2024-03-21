package com.ninekicks.microservices.controller

import com.ninekicks.microservices.service.impl.ProductDetailServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/product-browsing")
class ProductBrowsingController(
        private val productBrowsingService: ProductDetailServiceImpl
) {
    @GetMapping("/{productId}")
    fun displayUserProfile(@PathVariable productId:String): ResponseEntity<Any> {
        return productBrowsingService.fetchProductDetail(productId)
    }
}