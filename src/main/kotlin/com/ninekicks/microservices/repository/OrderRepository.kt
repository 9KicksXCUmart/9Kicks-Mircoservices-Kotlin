package com.ninekicks.microservices.repository

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import com.ninekicks.microservices.model.Order

interface OrderRepository {
    suspend fun getOrdersByUserId(userId: String, pageSize:Int, lastKey: Map<String, AttributeValue>?): List<Order>?
}