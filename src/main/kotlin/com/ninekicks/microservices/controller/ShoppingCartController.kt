package com.ninekicks.microservices.controller

import com.ninekicks.microservices.config.ResponseHandler
import com.ninekicks.microservices.model.dto.ShoppingCartUpdateDTO
import com.ninekicks.microservices.model.dto.UserUpdateDTO
import com.ninekicks.microservices.service.impl.GetAuthenticationServiceImpl
import com.ninekicks.microservices.service.impl.ShoppingCartServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/shopping-cart")
class ShoppingCartController(
    private val shoppingCartService:ShoppingCartServiceImpl,
    private val authenticationService: GetAuthenticationServiceImpl
) {
    val responseHandler = ResponseHandler()

    @GetMapping("")
    fun fetchShoppingCartDetail(): ResponseEntity<Any> {
        return shoppingCartService.fetchShoppingCartItems(authenticationService.getUserId())
    }

    @PatchMapping("/update")
    fun updateShoppingCartDetail(@RequestBody shoppingCartUpdateDTO:ShoppingCartUpdateDTO): ResponseEntity<Any> {
        return shoppingCartService.updateShoppingCartDetail(shoppingCartUpdateDTO,authenticationService.getUserId())
    }

    @PatchMapping("/delete")
    fun deleteShoppingCartItem(@RequestParam("itemId") itemId:String): ResponseEntity<Any>{
        return shoppingCartService.deleteShoppingCartItem(authenticationService.getUserId(),itemId)
    }

    @PatchMapping("/clear")
    fun clearShoppingCartItem(): ResponseEntity<Any>{
        return shoppingCartService.clearShoppingCartItems(authenticationService.getUserId())
    }
    
    @GetMapping("/validCheck")
    fun shoppingCartItemCheck(): ResponseEntity<Any>{
        return shoppingCartService.shoppingCartItemCheck(authenticationService.getUserId())
    }
}