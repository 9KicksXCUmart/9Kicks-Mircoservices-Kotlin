package com.ninekicks.microservices.model.dto

import com.ninekicks.microservices.model.Order
import com.ninekicks.microservices.model.Product
import com.ninekicks.microservices.model.User
import com.ninekicks.microservices.model.enum.DeliveryStatus
import com.ninekicks.microservices.model.enum.DeliveryType
import com.ninekicks.microservices.model.enum.OrderStatus
import java.time.LocalDateTime

data class OrderDetailDTO (
    val userId: String = "USER#",
    val orderId: String = "ORDER#",
    var orderStatus: Enum<OrderStatus>,
    var deliveryStatus: Enum<DeliveryStatus>,
    var orderDate: LocalDateTime,
    var receivedDate: LocalDateTime? = null,
    var orderItemDetail: List<Order.OrderItemDetail>? = null,
    var totalPrice: Float,
    var shippingAddress: User.ShippingAddress,
    var deliveryType: Enum<DeliveryType>,
) {

}