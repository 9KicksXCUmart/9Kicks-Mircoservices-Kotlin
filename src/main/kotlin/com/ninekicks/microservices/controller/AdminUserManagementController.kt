package com.ninekicks.microservices.controller

import com.ninekicks.microservices.model.User
import com.ninekicks.microservices.model.dto.OrderUpdateDTO
import com.ninekicks.microservices.model.dto.UserUpdateDTO
import com.ninekicks.microservices.service.impl.AdminUserManagementServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user-management")
class AdminUserManagementController(
    private val adminUserManagementService: AdminUserManagementServiceImpl
) {
    @GetMapping("/users")
    suspend fun displayAllUsers(
        @RequestParam("pagesize") pageSize: Int?,
        @RequestParam("lastkey") lastKey: String?
    ): ResponseEntity<Any> {
        return adminUserManagementService.findAllUsers(pageSize ?: 10, lastKey)
    }

    @GetMapping("/{userId}")
    suspend fun displayUser(@PathVariable userId: String): ResponseEntity<Any> {
        return adminUserManagementService.findUserById(userId)
    }

    @PatchMapping("/update", consumes = ["application/json"])
    suspend fun updateUser(@RequestBody userUpdateDTO: UserUpdateDTO): ResponseEntity<Any> {
        return adminUserManagementService.updateUser(userUpdateDTO)
    }

    @DeleteMapping("/delete/{userId}")
    suspend fun deleteUser(@PathVariable userId: String): ResponseEntity<Any> {
        return adminUserManagementService.deleteUser(userId)
    }

    @PostMapping("/create")
    suspend fun createUser(@RequestBody userUpdateDto: UserUpdateDTO): ResponseEntity<Any> {
        return adminUserManagementService.createUser(userUpdateDto)
    }

    @GetMapping("/order/{userId}")
    suspend fun displayOrdersByUserId(
        @PathVariable userId: String,
        @RequestParam("pagesize") pageSize: Int?,
        @RequestParam("lastkey") lastKey: String?
    ): ResponseEntity<Any> {
        return adminUserManagementService.findOrdersByUserId(userId, pageSize ?: 10, lastKey)
    }

    @PatchMapping("/update-order", consumes = ["application/json"])
    suspend fun updateOrder(@RequestBody orderUpdateDto: OrderUpdateDTO): ResponseEntity<Any> {
        return adminUserManagementService.updateOrder(orderUpdateDto)
    }

}