package com.ninekicks.microservices.controller

import com.ninekicks.microservices.service.impl.AccountSummaryServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/account-summary")
class AccountSummaryController (
    private val accountSummaryService: AccountSummaryServiceImpl,
) {
    @GetMapping("/{userId}")
    fun displayUserProfile(@PathVariable userId:String): ResponseEntity<Any> {
        return accountSummaryService.displayUserProfile(userId)
    }

    @PutMapping("/{userId}")
    fun updateUserProfile(@PathVariable userId:String): ResponseEntity<Any> {
        TODO()
    }

    @GetMapping("/order-history")
    fun displayOrderHistory(): ResponseEntity<Any> {
        TODO()
    }

    @GetMapping("/order-details")
    fun displayOrderDetails(): ResponseEntity<Any> {
        TODO()
    }

}