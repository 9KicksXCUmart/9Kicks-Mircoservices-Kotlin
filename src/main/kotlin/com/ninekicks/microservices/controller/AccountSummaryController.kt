package com.ninekicks.microservices.controller

import com.ninekicks.microservices.config.ResponseHandler
import com.ninekicks.microservices.model.User
import com.ninekicks.microservices.service.impl.AccountSummaryServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/account-summary")
class AccountSummaryController (
    private val accountSummaryService: AccountSummaryServiceImpl
) {
    val responseHandler = ResponseHandler()

    @GetMapping("/user-profile/{userId}")
    fun displayUserProfile(@PathVariable userId:String): ResponseEntity<Any> {
        return try{
            val result: User = accountSummaryService.displayUserProfile(userId)
                ?: return responseHandler.generateResponse("User not found", false, null)
            responseHandler.generateResponse("Successful", true, result)
        }catch (e: Exception) {
            responseHandler.generateResponse(e.message ?: "", false, null)
        }
    }

}