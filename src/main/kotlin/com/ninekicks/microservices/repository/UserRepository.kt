package com.ninekicks.microservices.repository

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import com.ninekicks.microservices.model.User

interface UserRepository {
    suspend fun getUser(userId: String): User?
    suspend fun getAllUsers(
        pageSize:Int,
        lastKey: Map<String, AttributeValue>?
    ): List<User>?
    suspend fun addUser(user: User): User?
    suspend fun updateUser(user: User): User?
    suspend fun deleteUser(userId: String): Boolean?
}