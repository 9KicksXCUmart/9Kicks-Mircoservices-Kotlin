package com.ninekicks.microservices.controller

import com.ninekicks.microservices.config.ResponseHandler
import com.ninekicks.microservices.model.dto.ShoppingCartUpdateDTO
import com.ninekicks.microservices.model.dto.UserUpdateDTO
import com.ninekicks.microservices.service.impl.ShoppingCartServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/shopping-cart")
class ShoppingCartController(
    private val shoppingCartService:ShoppingCartServiceImpl
) {
    val responseHandler = ResponseHandler()

    @GetMapping("")
    fun fetchShoppingCartDetail(@RequestParam("userId") userId:String): ResponseEntity<Any> {
        return shoppingCartService.fetchShoppingCartItems(userId)
    }

    @PatchMapping("/update")
    fun updateShoppingCartDetail(@RequestBody shoppingCartUpdateDTO:ShoppingCartUpdateDTO): ResponseEntity<Any> {
        return shoppingCartService.updateShoppingCartDetail(shoppingCartUpdateDTO)
    }

    @PatchMapping("/delete")
    fun deleteShoppingCartItem(@RequestParam("userId") userId:String,@RequestParam("itemId") itemId:String): ResponseEntity<Any>{
        return shoppingCartService.deleteShoppingCartItem(userId,itemId)
    }

    @PatchMapping("/clear")
    fun clearShoppingCartItem(@RequestParam("userId")userId:String): ResponseEntity<Any>{
        return shoppingCartService.clearShoppingCartItems(userId)
    }
}