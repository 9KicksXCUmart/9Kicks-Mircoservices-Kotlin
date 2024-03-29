package com.ninekicks.microservices.model.dto

import com.ninekicks.microservices.model.enum.DeliveryStatus
import com.ninekicks.microservices.model.enum.OrderStatus

data class OrderUpdateDTO(
    val userId: String = "USER#",
    val orderId: String = "ORDER#",
    var orderStatus: OrderStatus?,
    var deliveryStatus: DeliveryStatus?
)
