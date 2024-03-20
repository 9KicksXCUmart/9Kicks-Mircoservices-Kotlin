package com.ninekicks.microservices.repository

import com.ninekicks.microservices.model.Product

interface ProductRepository {
    suspend fun getProductDetail(productId: String): Product?
}