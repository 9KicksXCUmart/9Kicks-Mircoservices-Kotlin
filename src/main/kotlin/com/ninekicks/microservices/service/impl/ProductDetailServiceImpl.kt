package com.ninekicks.microservices.service.impl

import com.ninekicks.microservices.config.AppConfig
import com.ninekicks.microservices.config.ResponseHandler
import com.ninekicks.microservices.model.Product
import com.ninekicks.microservices.model.ShoppingCart
import com.ninekicks.microservices.model.dto.ProductDiscountPriceDTO
import com.ninekicks.microservices.model.dto.ProductResponseDTO
import com.ninekicks.microservices.model.dto.ValidationResponseDTO
import com.ninekicks.microservices.repository.impl.ProductRepositoryImpl
import com.ninekicks.microservices.service.GetAuthenticationService
import com.ninekicks.microservices.service.ProductDetailService
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate

@Repository
class ProductDetailServiceImpl(
        private val productRepository: ProductRepositoryImpl,
        private val appConfig: AppConfig
):ProductDetailService {

    private val responseHandler = ResponseHandler()
    // Get product detail using productId (for testing purpose)
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
    // GET product price info from other mirco-service
    override fun fetchProductDiscountedPrice(productId: String): ProductDiscountPriceDTO? {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        val entity = HttpEntity<String>("", headers)
        val response = restTemplate.exchange(
            "${appConfig.goBackendUrl}/v1/products/${productId}",
            HttpMethod.GET,
            entity,
            ProductResponseDTO::class.java
        )
       return ProductDiscountPriceDTO(
            price =response.body!!.data!!.price,
            isDiscounted =response.body!!.data!!.isDiscount,
            discountPrice = response.body!!.data!!.discountPrice
        )

    }
}

