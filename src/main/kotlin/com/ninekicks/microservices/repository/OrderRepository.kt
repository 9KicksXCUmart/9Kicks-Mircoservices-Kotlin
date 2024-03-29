package com.ninekicks.microservices.repository

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import com.ninekicks.microservices.model.Order
import com.ninekicks.microservices.model.dto.OrderUpdateDTO

interface OrderRepository {
    suspend fun getOrdersByUserId(userId: String, pageSize:Int?, lastKey: Map<String, AttributeValue>?): List<Order>?
    suspend fun getOrder(userId: String, orderId: String): Order?
    suspend fun updateOrder(orderUpdateDto: OrderUpdateDTO): Order?
}