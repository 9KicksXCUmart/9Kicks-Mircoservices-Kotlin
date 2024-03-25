package com.ninekicks.microservices.service

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import org.springframework.http.ResponseEntity

interface AdminUserManagementService {
    suspend fun findAllUsers(pageSize: Int,  lastKey: Map<String, AttributeValue>?):ResponseEntity<Any>
    suspend fun findUserById(userId:String):ResponseEntity<Any>
}