package com.ninekicks.microservices.repository

interface UserRepository {
    suspend fun getAllTable()
}