package com.ninekicks.microservices.demo

interface DemoRepository {
    suspend fun getAllTable(): List<String>?
}