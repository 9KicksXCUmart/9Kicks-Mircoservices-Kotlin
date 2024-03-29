package com.ninekicks.microservices.model

import com.amazonaws.services.dynamodbv2.datamodeling.*
import com.ninekicks.microservices.helper.ListConverter
import com.ninekicks.microservices.model.enum.DeliveryStatus
import com.ninekicks.microservices.model.enum.OrderStatus
import java.time.LocalDateTime

@DynamoDBTable(tableName ="9Kicks")
data class Order(
    @DynamoDBHashKey(attributeName = "PK")
    var userId: String = "USER#",
    @DynamoDBRangeKey(attributeName = "SK")
    val orderId: String = "ORDER#",
    @DynamoDBAttribute(attributeName = "orderStatus")
    var orderStatus: Enum<OrderStatus>,
    @DynamoDBAttribute(attributeName = "deliveryStatus")
    var deliveryStatus: Enum<DeliveryStatus>,
    @DynamoDBAttribute(attributeName = "orderDate")
    var orderDate: LocalDateTime,
    @DynamoDBAttribute(attributeName = "receivedDate")
    var receivedDate: LocalDateTime? = null,
    @DynamoDBAttribute(attributeName = "orderItemDetail")
    @DynamoDBTypeConverted(converter = ListConverter::class)
    var orderItemDetail: List<OrderItemDetail>? = null,
    @DynamoDBAttribute(attributeName = "totalPrice")
    var totalPrice: Float
) {
    data class OrderItemDetail(
        var productId: String,
        var sizeQuantity: Map<String, Int>,
    )
}
