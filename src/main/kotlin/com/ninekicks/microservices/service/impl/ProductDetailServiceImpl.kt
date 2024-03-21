package com.ninekicks.microservices.service.impl

import com.ninekicks.microservices.config.ResponseHandler
import com.ninekicks.microservices.repository.impl.ProductRepositoryImpl
import com.ninekicks.microservices.service.ProductDetailService
import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository

@Repository
class ProductDetailServiceImpl(
        private val productRepository: ProductRepositoryImpl
):ProductDetailService {
    override fun fetchProductDetail(productId: String): ResponseEntity<Any> {
        val responseHandler = ResponseHandler()
        return runBlocking {
            var product = productRepository.getProductDetail(productId)
            responseHandler.validateResponse(
                failMessage = "Product not found",
                matchingObject = product,
                failObject = null
            )
        }

    }
}

