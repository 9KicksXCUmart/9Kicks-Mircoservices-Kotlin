package com.ninekicks.microservices.model.dto

import com.ninekicks.microservices.model.Order
import com.ninekicks.microservices.model.User
import com.ninekicks.microservices.model.enum.DeliveryStatus
import com.ninekicks.microservices.model.enum.OrderStatus
import java.time.LocalDateTime

class OrderCreateDTO(
    var userId: String = "USER#",
    val orderId: String = "ORDER#",
    var orderStatus: String,
    var deliveryStatus:String,
    var orderItemDetail: List<Order.OrderItemDetail>? = null,
    var totalPrice: Float,
    var shippingAddress: User.ShippingAddress,
    var deliveryType: String
){
}