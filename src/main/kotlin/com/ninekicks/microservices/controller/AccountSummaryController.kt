package com.ninekicks.microservices.controller

import com.ninekicks.microservices.model.dto.UserUpdateDTO
import com.ninekicks.microservices.service.impl.AccountSummaryServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/account-summary")
class AccountSummaryController(
    private val accountSummaryService: AccountSummaryServiceImpl,
) {
    @GetMapping("/{userId}")
    fun displayUserProfile(@PathVariable userId: String): ResponseEntity<Any> {
        return accountSummaryService.displayUserDetails(userId)
    }

    @PatchMapping("/update-profile", consumes = ["application/json"])
    fun updateUserProfile(@RequestBody userUpdateDTO: UserUpdateDTO): ResponseEntity<Any> {
        return accountSummaryService.updateUserDetails(userUpdateDTO)
    }

    @GetMapping("/order-details")
    fun displayOrderDetails(
        @RequestParam("userId") userId: String,
        @RequestParam("orderId") orderId: String
    ): ResponseEntity<Any> {
        return accountSummaryService.displayOrderDetails(userId, orderId)
    }

    @GetMapping("/order-history/{userId}")
    fun displayOrderHistory(
        @PathVariable userId: String,
        @RequestParam("pagesize") pageSize: Int?,
        @RequestParam("lastkey") lastKey: String?
    ): ResponseEntity<Any> {
        return accountSummaryService.listOrdersByUserId(userId, pageSize?:10, lastKey)
    }

}