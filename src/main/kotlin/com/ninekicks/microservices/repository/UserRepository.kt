package com.ninekicks.microservices.repository

import com.ninekicks.microservices.model.User

interface UserRepository {
    suspend fun getUser(userId: String): User?
}