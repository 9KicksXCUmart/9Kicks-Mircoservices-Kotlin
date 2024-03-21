package com.ninekicks.microservices.service.impl

import com.ninekicks.microservices.config.ResponseHandler
import com.ninekicks.microservices.repository.impl.UserRepositoryImpl
import com.ninekicks.microservices.service.AccountSummaryService
import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class AccountSummaryServiceImpl(
    private val userRepository: UserRepositoryImpl
): AccountSummaryService {
    private val responseHandler = ResponseHandler()
    override fun displayUserProfile(userId:String): ResponseEntity<Any> {
        return runBlocking {
            val user = userRepository.getUser(userId)
            responseHandler.validateResponse(
                failMessage = "User not found",
                matchingObject = user,
                failReturnObject = null
            )
        }
    }
}