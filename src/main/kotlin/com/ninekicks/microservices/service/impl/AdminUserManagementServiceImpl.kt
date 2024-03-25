package com.ninekicks.microservices.service.impl

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import com.ninekicks.microservices.config.ResponseHandler
import com.ninekicks.microservices.model.User
import com.ninekicks.microservices.repository.impl.UserRepositoryImpl
import com.ninekicks.microservices.service.AdminUserManagementService
import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class AdminUserManagementServiceImpl(
    private val userRepository: UserRepositoryImpl
): AdminUserManagementService {
    private val responseHandler = ResponseHandler()

    override suspend fun findAllUsers(
        pageSize: Int,
        lastKey: Map<String, AttributeValue>?
    ): ResponseEntity<Any> {

        return runBlocking {
            val userList = userRepository.getAllUsers(pageSize, null)

            responseHandler.validateResponse(
                failMessage = "User not found",
                matchingObject = userList,
                failReturnObject = listOf<User>()
            )
        }
    }

    override suspend fun findUserById(userId: String): ResponseEntity<Any> {
        TODO("Not yet implemented")
    }
}