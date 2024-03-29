package com.ninekicks.microservices.service.impl

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import com.ninekicks.microservices.config.ResponseHandler
import com.ninekicks.microservices.model.User
import com.ninekicks.microservices.model.dto.OrderUpdateDTO
import com.ninekicks.microservices.model.dto.UserUpdateDTO
import com.ninekicks.microservices.repository.impl.OrderRepositoryImpl
import com.ninekicks.microservices.repository.impl.UserRepositoryImpl
import com.ninekicks.microservices.service.AdminUserManagementService
import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class AdminUserManagementServiceImpl(
    private val userRepository: UserRepositoryImpl,
    private val orderRepository: OrderRepositoryImpl
): AdminUserManagementService {
    private val responseHandler = ResponseHandler()

    override suspend fun findAllUsers(
        pageSize: Int,
        lastUserKey: String?
    ): ResponseEntity<Any> {

        val lastEvaluatedKey = lastUserKey?.let {
            mapOf(
                "PK" to AttributeValue.S("USER#$lastUserKey"),
                "SK" to AttributeValue.S("USER_PROFILE")
            )
        }

        return runBlocking {
            val userList = userRepository.getAllUsers(pageSize, lastEvaluatedKey)

            responseHandler.validateResponse(
                failMessage = "User not found",
                matchingObject = userList,
                failReturnObject = listOf<User>()
            )
        }
    }

    override suspend fun findUserById(userId: String): ResponseEntity<Any> {
        return runBlocking {
            val user = userRepository.getUser(userId)

            responseHandler.validateResponse(
                failMessage = "User not found",
                matchingObject = user,
                failReturnObject = null
            )
        }
    }

    override suspend fun createUser(userUpdateDTO: UserUpdateDTO): ResponseEntity<Any> {
        return runBlocking {
            val user = userRepository.addUser(userUpdateDTO)

            responseHandler.validateResponse(
                failMessage = "Unable to Create User",
                matchingObject = user,
                failReturnObject = null
            )
        }
    }

    override suspend fun deleteUser(userId: String): ResponseEntity<Any> {
        return runBlocking {
            println("delete user: $userId")
            val isDeleted = userRepository.deleteUser(userId)

            responseHandler.validateResponse(
                failMessage = "Unable to Delete User",
                matchingObject = isDeleted,
                failReturnObject = null
            )
        }
    }

    override suspend fun updateUser(userUpdateDTO: UserUpdateDTO): ResponseEntity<Any> {
        return runBlocking {
            val user = userRepository.updateUser(userUpdateDTO)
            responseHandler.validateResponse(
                failMessage = "No user found",
                matchingObject = user,
                failReturnObject = null
            )
        }
    }

    override suspend fun findOrdersByUserId(userId: String, pageSize: Int, lastOrderKey: String?): ResponseEntity<Any> {

        val lastEvaluatedKey = lastOrderKey?.let {
            mapOf(
                "PK" to AttributeValue.S("USER#$userId"),
                "SK" to AttributeValue.S("ORDER#$lastOrderKey")
            )
        }

        return runBlocking {
            val user = orderRepository.getOrdersByUserId(userId, pageSize, lastEvaluatedKey)
            responseHandler.validateResponse(
                failMessage = "No Order found",
                matchingObject = user,
                failReturnObject = null
            )
        }
    }

    override suspend fun updateOrder(orderUpdateDto: OrderUpdateDTO): ResponseEntity<Any> {
        return runBlocking {
            val order = orderRepository.updateOrder(orderUpdateDto)
            responseHandler.validateResponse(
                failMessage = "Fail to Update Order",
                matchingObject = order,
                failReturnObject = null
            )
        }
    }
}