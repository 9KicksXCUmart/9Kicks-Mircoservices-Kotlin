package com.ninekicks.microservices.controller

import com.ninekicks.microservices.model.dto.UserUpdateDTO
import com.ninekicks.microservices.service.impl.AccountSummaryServiceImpl
import com.ninekicks.microservices.service.impl.GetAuthenticationServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/account-summary")
class AccountSummaryController(
    private val accountSummaryService: AccountSummaryServiceImpl,
    private val authenticationService: GetAuthenticationServiceImpl
) {
    @GetMapping("")
    fun displayUserProfile(): ResponseEntity<Any> {
        println(authenticationService.getUserId())
        return accountSummaryService.displayUserDetails(authenticationService.getUserId())
    }

    @PatchMapping("/update-profile", consumes = ["application/json"])
    fun updateUserProfile(@RequestBody userUpdateDTO: UserUpdateDTO): ResponseEntity<Any> {
        return accountSummaryService.updateUserDetails(authenticationService.getUserId(), userUpdateDTO)
    }

    @GetMapping("/order-details")
    fun displayOrderDetails(
        @RequestParam("orderId") orderId: String
    ): ResponseEntity<Any> {
        return accountSummaryService.displayOrderDetails(authenticationService.getUserId(), orderId)
    }

    @GetMapping("/order-history")
    fun displayOrderHistory(
        @RequestParam("pagesize") pageSize: Int?,
        @RequestParam("lastkey") lastKey: String?
    ): ResponseEntity<Any> {
        return accountSummaryService.listOrdersByUserId(authenticationService.getUserId(), pageSize?:10, lastKey)
    }
}