package com.ninekicks.microservices.service.impl

import com.ninekicks.microservices.config.ResponseHandler
import com.ninekicks.microservices.model.Product
import com.ninekicks.microservices.model.ShoppingCart
import com.ninekicks.microservices.model.dto.ProductDiscountPriceDTO
import com.ninekicks.microservices.repository.impl.ProductRepositoryImpl
import com.ninekicks.microservices.service.ProductDetailService
import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository

@Repository
class ProductDetailServiceImpl(
        private val productRepository: ProductRepositoryImpl
):ProductDetailService {

    private val responseHandler = ResponseHandler()

    override fun fetchProductDetail(productId: String): ResponseEntity<Any> {
        return runBlocking {
            val product = productRepository.getProductDetail(productId)
            responseHandler.validateResponse(
                failMessage = "Product not found",
                matchingObject = product,
                failReturnObject = null
            )
        }
    }

    override fun fetchProductDiscountedPrice(productId: String): ProductDiscountPriceDTO? {
        return runBlocking {
            val product = productRepository.getProductDetail(productId.replace("PRODUCT#",""))
             ProductDiscountPriceDTO(
                price = product!!.price,
                isDiscounted = product!!.isDiscount,
                discountPrice = product?.discountPrice
            )
        }

    }
}

