package com.ninekicks.microservices.controller

import com.ninekicks.microservices.repository.impl.DemoRepositoryImpl
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class DemoController(
    private val demoController: DemoRepositoryImpl
    ) {

    @GetMapping("/helloworld")
    fun helloWorld():String {
        return "helloworld"
    }

    @GetMapping("/get-all-table")
    suspend fun getAllTable(): List<String>? {
        return try {
            demoController.getAllTable()
        } catch (e: Exception) {
            println("Error fetching table names: ${e.message}")
            listOf("Error")
        }
    }

}


