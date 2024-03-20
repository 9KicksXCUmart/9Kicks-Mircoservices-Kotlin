package com.ninekicks.microservices.service.impl

import com.ninekicks.microservices.model.Product
import com.ninekicks.microservices.repository.impl.ProductRepositoryImpl
import com.ninekicks.microservices.service.ProductDetailService
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Repository

@Repository
class ProductDetailServiceImpl(
        private val productRepository: ProductRepositoryImpl
):ProductDetailService {
    override fun fetchProductDetail(productId: String): Product? {
        var product: Product?
        runBlocking {product = productRepository.getProductDetail(productId) }
        return product
    }
}

