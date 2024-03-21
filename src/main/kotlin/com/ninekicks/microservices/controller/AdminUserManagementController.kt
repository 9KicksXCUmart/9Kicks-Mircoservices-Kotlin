package com.ninekicks.microservices.controller

import com.ninekicks.microservices.model.User
import com.ninekicks.microservices.repository.impl.UserRepositoryImpl
import com.ninekicks.microservices.service.impl.AdminUserManagementServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user-management")
class AdminUserManagementController(
    private val userRepo: UserRepositoryImpl,
    private val adminUserManagementService: AdminUserManagementServiceImpl
) {
    @GetMapping("/users")
    suspend fun displayAllUsers(): ResponseEntity<Any> {
        return adminUserManagementService.findAllUsers(10, null)
    }

    @PostMapping("/users")
    suspend fun updateUser():ResponseEntity<User> {
      TODO()
    }

    @DeleteMapping("/users/{userId}")
    suspend fun deleteUser():ResponseEntity<Any> {
      TODO()
    }
}