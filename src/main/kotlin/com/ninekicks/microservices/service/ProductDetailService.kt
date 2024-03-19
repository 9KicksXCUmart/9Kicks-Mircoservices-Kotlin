package com.ninekicks.microservices.service

import com.ninekicks.microservices.model.Product

interface ProductDetailService {
    fun fetchProductDetail(productId:String): Product?
}