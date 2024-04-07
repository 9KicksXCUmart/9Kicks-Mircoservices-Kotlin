package com.ninekicks.microservices.service.impl

import com.ninekicks.microservices.config.ResponseHandler
import com.ninekicks.microservices.model.dto.ShoppingCartUpdateDTO
import com.ninekicks.microservices.repository.impl.ProductRepositoryImpl
import com.ninekicks.microservices.repository.impl.UserRepositoryImpl
import com.ninekicks.microservices.service.ProductDetailService
import com.ninekicks.microservices.service.ShoppingCartService
import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository

@Repository
class ShoppingCartServiceImpl(
    private val userRepository: UserRepositoryImpl
): ShoppingCartService {
    private val responseHandler = ResponseHandler()

    override fun fetchShoppingCartItems(userId: String): ResponseEntity<Any> {
        return runBlocking {
            val shoppingCart = userRepository.getShoppingCartDeatil(userId)
            responseHandler.validateResponse(
                failMessage = "shoppingCart not found",
                matchingObject = shoppingCart,
                failReturnObject = null
            )
        }
    }

    override fun updateShoppingCartDetail(shoppingCartUpdateDTO: ShoppingCartUpdateDTO, userId: String): ResponseEntity<Any> {
        return runBlocking {
            val isAdded = userRepository.updateShoppingCartDeatil(shoppingCartUpdateDTO,userId)
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


}