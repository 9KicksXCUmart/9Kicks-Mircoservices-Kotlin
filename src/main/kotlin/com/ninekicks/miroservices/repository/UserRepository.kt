package com.ninekicks.micoservices.repository

interface UserRepository {
    suspend fun getAllTable(): String
}