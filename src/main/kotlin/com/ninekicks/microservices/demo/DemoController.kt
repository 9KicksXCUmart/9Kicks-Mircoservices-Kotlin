package com.ninekicks.microservices.demo

import com.ninekicks.microservices.model.User
import com.ninekicks.microservices.model.dto.UserUpdateDTO
import com.ninekicks.microservices.repository.impl.OrderRepositoryImpl
import com.ninekicks.microservices.repository.impl.ProductRepositoryImpl
import com.ninekicks.microservices.repository.impl.UserRepositoryImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping
class DemoController(
    private val demoController: DemoRepositoryImpl,
    private val userRepository: UserRepositoryImpl,
    private val orderRepository: OrderRepositoryImpl,
    private val productRepository: ProductRepositoryImpl
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
//    @PutMapping("/test", consumes=["application/json"])
//    suspend fun test(@RequestBody userDto: UserUpdateDTO): User? {
//        return userRepository.updateUser(userDto)
//    }
}


