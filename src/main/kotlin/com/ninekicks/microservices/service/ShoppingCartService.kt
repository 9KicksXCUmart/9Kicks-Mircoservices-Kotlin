package com.ninekicks.microservices.service

import com.ninekicks.microservices.model.dto.ShoppingCartUpdateDTO
import org.springframework.http.ResponseEntity

interface ShoppingCartService {
    fun fetchShoppingCartItems(userId: String): ResponseEntity<Any>
    fun updateShoppingCartDetail(shoppingCartUpdateDTO: ShoppingCartUpdateDTO, userId: String): ResponseEntity<Any>
    fun deleteShoppingCartItem(userId: String, itemId: String): ResponseEntity<Any>
    fun clearShoppingCartItems(userId:String): ResponseEntity<Any>
    fun shoppingCartItemCheck(userId:String): ResponseEntity<Any>
}