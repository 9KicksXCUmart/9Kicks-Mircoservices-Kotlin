package com.ninekicks.microservices.service


import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import com.ninekicks.microservices.model.User
import org.springframework.http.ResponseEntity

interface AccountSummaryService{
    fun displayUserDetails(userId:String): ResponseEntity<Any>
    fun listOrdersByUserId(userId:String, pageSize:Int, lastKey:Map<String, AttributeValue>?): ResponseEntity<Any>
    fun displayOrderDetails(userId:String, orderId:String): ResponseEntity<Any>
    fun updateUserDetails(user: User): ResponseEntity<Any>
}