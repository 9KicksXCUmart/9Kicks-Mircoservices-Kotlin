package com.ninekicks.microservices.service

import com.ninekicks.microservices.model.dto.OrderUpdateDTO
import com.ninekicks.microservices.model.dto.UserUpdateDTO
import org.springframework.http.ResponseEntity

interface AdminUserManagementService {
    suspend fun findAllUsers(pageSize: Int,  lastUserKey: String?):ResponseEntity<Any>
    suspend fun findUserById(userId:String):ResponseEntity<Any>
    suspend fun createUser(userUpdateDto: UserUpdateDTO):ResponseEntity<Any>
    suspend fun deleteUser(userId: String):ResponseEntity<Any>
    suspend fun updateUser(userUpdateDTO: UserUpdateDTO):ResponseEntity<Any>
    suspend fun findOrdersByUserId(userId: String, pageSize: Int, lastOrderKey: String?): ResponseEntity<Any>
    suspend fun updateOrder(orderUpdateDto: OrderUpdateDTO): ResponseEntity<Any>
}