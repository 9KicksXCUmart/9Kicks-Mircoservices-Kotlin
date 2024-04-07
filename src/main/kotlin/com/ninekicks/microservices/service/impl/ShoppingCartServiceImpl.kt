package com.ninekicks.microservices.service.impl

import com.ninekicks.microservices.config.AppConfig
import com.ninekicks.microservices.config.ResponseHandler
import com.ninekicks.microservices.model.ShoppingCart
import com.ninekicks.microservices.model.dto.ShoppingCartUpdateDTO
import com.ninekicks.microservices.model.dto.StockResponseDTO
import com.ninekicks.microservices.repository.impl.UserRepositoryImpl
import com.ninekicks.microservices.service.ShoppingCartService
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate

@Repository
class ShoppingCartServiceImpl(
    private val userRepository: UserRepositoryImpl,
    private val appConfig: AppConfig
): ShoppingCartService {
    private val responseHandler = ResponseHandler()

    override fun fetchShoppingCartItems(userId: String): ResponseEntity<Any> {
        return runBlocking {
            val shoppingCart = userRepository.getShoppingCartDetail(userId)
            responseHandler.validateResponse(
                failMessage = "shoppingCart not found",
                matchingObject = shoppingCart,
                failReturnObject = null
            )
        }
    }

    override fun updateShoppingCartDetail(shoppingCartUpdateDTO: ShoppingCartUpdateDTO, userId: String): ResponseEntity<Any> {
        return runBlocking {
            val isAdded = userRepository.updateShoppingCartDetail(shoppingCartUpdateDTO,userId)
            responseHandler.validateResponse(
                failMessage = "shoppingCart not found",
                matchingObject = isAdded,
                failReturnObject = null
            )
        }
    }

    override fun deleteShoppingCartItem(userId: String, itemId: String): ResponseEntity<Any> {
        return runBlocking {
            val isDeleted = userRepository.deleteShoppingCartItem(userId,itemId)
            responseHandler.validateResponse(
                failMessage = "shoppingCart not found",
                matchingObject = isDeleted,
                failReturnObject = null
            )
        }
    }

    override fun clearShoppingCartItems(userId: String): ResponseEntity<Any> {
        return runBlocking {
            val isCleared = userRepository.clearShoppingCartItems(userId)
            responseHandler.validateResponse(
                failMessage = "shoppingCart not found",
                matchingObject = isCleared,
                failReturnObject = null
            )
        }
    }

    override fun shoppingCartItemCheck(userId: String): ResponseEntity<Any> {
        var list:MutableList<String> = ArrayList()
        val shoppingCart:ShoppingCart?
        runBlocking {
        shoppingCart = userRepository.getShoppingCartDetail(userId)
        }
        shoppingCart?.shoppingCartItemDetail?.forEach{
            val restTemplate = RestTemplate()
            val headers = HttpHeaders()
            val entity = HttpEntity<String>("", headers)
            val response = restTemplate.exchange(
                "${appConfig.goBackendUrl}/v1/products/${it.value.productId}/stock?size=${it.value.productSize}",
                HttpMethod.GET,
                entity,
                StockResponseDTO::class.java
            )
            if(response.body!!.data!!.remainingStock < it.value.productQuantity )
                list.add(it.key)
        }
        return responseHandler.validateResponse(
                failMessage = "Unable to check stock",
                matchingObject = list,
                failReturnObject = null
            )

    }


}

