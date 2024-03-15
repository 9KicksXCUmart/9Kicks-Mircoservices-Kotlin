package com.ninekicks.microservices.repository

interface DemoRepository {
    suspend fun getAllTable(): List<String>?
}