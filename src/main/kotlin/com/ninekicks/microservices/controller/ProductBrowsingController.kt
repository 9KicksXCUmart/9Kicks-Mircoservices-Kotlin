package com.ninekicks.microservices.controller

import com.ninekicks.microservices.config.ResponseHandler
import com.ninekicks.microservices.model.dto.ProductDiscountPriceDTO

import com.ninekicks.microservices.service.impl.ProductDetailServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/product-browsing")
class ProductBrowsingController(
        private val productBrowsingService: ProductDetailServiceImpl
) {
    @GetMapping("")
    fun fetchProductDetail(@RequestParam("productId") productId:String): ResponseEntity<Any> {
        return productBrowsingService.fetchProductDetail(productId)
    }

    @GetMapping("/test")
    fun fetchProductDiscountedPrice(@RequestParam("productId") productId:String): ProductDiscountPriceDTO? {
        return productBrowsingService.fetchProductDiscountedPrice(productId)
    }

}