package com.ninekicks.microservices.service


import com.ninekicks.microservices.model.dto.UserUpdateDTO
import org.springframework.http.ResponseEntity

interface AccountSummaryService{
    fun displayUserDetails(userId:String): ResponseEntity<Any>
    fun listOrdersByUserId(userId:String, pageSize:Int, lastOrderKey:String?): ResponseEntity<Any>
    fun displayOrderDetails(userId:String, orderId:String): ResponseEntity<Any>
    fun updateUserDetails(userUpdateDTO: UserUpdateDTO): ResponseEntity<Any>
}