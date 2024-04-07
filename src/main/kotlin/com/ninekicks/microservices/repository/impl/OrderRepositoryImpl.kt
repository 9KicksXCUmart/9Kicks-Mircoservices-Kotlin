package com.ninekicks.microservices.repository.impl

import aws.sdk.kotlin.services.dynamodb.model.*
import com.ninekicks.microservices.config.DynamoDBConfig
import com.ninekicks.microservices.helper.converter.OrderItemDetailListConverter
import com.ninekicks.microservices.helper.converter.ShippingAddressConverter
import com.ninekicks.microservices.model.Order
import com.ninekicks.microservices.model.dto.OrderCreateDTO
import com.ninekicks.microservices.model.dto.OrderDetailDTO
import com.ninekicks.microservices.model.dto.OrderUpdateDTO
import com.ninekicks.microservices.model.enum.DeliveryStatus
import com.ninekicks.microservices.model.enum.DeliveryType
import com.ninekicks.microservices.model.enum.OrderStatus
import com.ninekicks.microservices.repository.OrderRepository
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Repository
class OrderRepositoryImpl(
    private val dynamoDBConfig: DynamoDBConfig
) : OrderRepository {
    private val dynamoDbClient by lazy { dynamoDBConfig.dynamoDbClient() }

    @Value("\${dynamodb.tableName}")
    private val dynamoDbtableName: String? = null
    private val orderItemDetailListConverter = OrderItemDetailListConverter()
    private val shippingAddressConverter = ShippingAddressConverter()

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("d/M/yyyy H:mm")

    private val keyToGet = mutableMapOf<String, AttributeValue>()

    override suspend fun getOrdersByUserId(
        userId: String,
        pageSize: Int?,
        lastKey: Map<String, AttributeValue>?
    ): List<Order>? {
        val queryRequest = QueryRequest {
            this.tableName = dynamoDbtableName
            keyConditionExpression = "PK = :pk AND begins_with(SK, :sk)"
            expressionAttributeValues = mapOf(
                ":pk" to AttributeValue.S("USER#$userId"),
                ":sk" to AttributeValue.S("ORDER#")
            )
            this.exclusiveStartKey = lastKey
            this.limit = pageSize
        }
        val queryResult = dynamoDbClient.query(queryRequest)

        return try {
            queryResult.items?.map { itemMap ->
                Order(
                    userId = itemMap["PK"]!!.asS(),
                    orderId = itemMap["SK"]!!.asS(),
                    orderStatus = enumValueOf<OrderStatus>(itemMap["orderStatus"]!!.asS()),
                    deliveryStatus = enumValueOf<DeliveryStatus>(itemMap["deliveryStatus"]!!.asS()),
                    orderDate = LocalDateTime.parse(itemMap["orderDate"]!!.asS(), dateTimeFormatter),
                    receivedDate = itemMap["receivedDate"]?.asS()?.takeIf { it.isNotBlank() }
                        ?.let { LocalDateTime.parse(it, dateTimeFormatter) },
                    orderItemDetail = orderItemDetailListConverter.unconvert(itemMap["orderItemDetail"]?.asL())!!,
                    totalPrice = itemMap["totalPrice"]!!.asN().toFloat(),
                    shippingAddress = shippingAddressConverter.unconvert(itemMap["shippingAddress"]),
                    deliveryType = enumValueOf<DeliveryType>(itemMap["deliveryType"]!!.asS())
                    )
            }
        } catch (e: Exception) {
            println(e)
            null
        }
    }

    override suspend fun getOrder(userId: String, orderId: String): Order? {
        keyToGet["PK"] = AttributeValue.S("USER#$userId")
        keyToGet["SK"] = AttributeValue.S("ORDER#$orderId")

        val itemRequest = GetItemRequest {
            key = keyToGet
            tableName = dynamoDbtableName
        }

        return try {
            val returnedItem = dynamoDbClient.getItem(itemRequest)
            val itemMap: Map<String, AttributeValue> = returnedItem.item!!
            println(itemMap)
            Order(
                userId = itemMap["PK"]!!.asS(),
                orderId = itemMap["SK"]!!.asS(),
                orderStatus = enumValueOf<OrderStatus>(itemMap["orderStatus"]!!.asS()),
                deliveryStatus = enumValueOf<DeliveryStatus>(itemMap["deliveryStatus"]!!.asS()),
                orderDate = LocalDateTime.parse(itemMap["orderDate"]!!.asS(), dateTimeFormatter),
                receivedDate = itemMap["receivedDate"]?.asS()?.takeIf { it.isNotBlank() }
                    ?.let { LocalDateTime.parse(it, dateTimeFormatter) },
                orderItemDetail = orderItemDetailListConverter.unconvert(itemMap["orderItemDetail"]?.asL())!!,
                totalPrice = itemMap["totalPrice"]!!.asN().toFloat(),
                shippingAddress = shippingAddressConverter.unconvert(itemMap["shippingAddress"]),
                deliveryType = enumValueOf<DeliveryType>(itemMap["deliveryType"]!!.asS())
            )
        } catch (e: Exception) {
            println(e)
            null
        }
    }

    override suspend fun updateOrder(orderUpdateDto: OrderUpdateDTO): Order? {
        keyToGet["PK"] = AttributeValue.S("USER#${orderUpdateDto.userId}")
        keyToGet["SK"] = AttributeValue.S("ORDER#${orderUpdateDto.orderId}")

        val attributeUpdates = mapOf(
            "orderStatus" to orderUpdateDto.orderStatus?.let { AttributeValue.S(it.toString()) },
            "deliveryStatus" to orderUpdateDto.deliveryStatus?.let { AttributeValue.S(it.toString()) },
        ).mapNotNull { (key, value) -> value?.let { key to it } }.toMap()

        val updateExpression = "SET " + attributeUpdates.keys.joinToString(", ") { "$it = :$it" }
        val expressionAttributeValues = attributeUpdates.entries.associate { (key, value) -> ":$key" to value }

        val updateItemRequest = UpdateItemRequest {
            this.tableName = dynamoDbtableName
            this.key = keyToGet
            this.updateExpression = updateExpression
            this.expressionAttributeValues = expressionAttributeValues
        }

        dynamoDbClient.updateItem(updateItemRequest)
        return getOrder(orderUpdateDto.userId, orderUpdateDto.orderId)
    }

    override suspend fun createOrder(orderDetail: OrderCreateDTO,userId: String): Order? {
        try {

        val itemValues = mapOf(
            "PK" to AttributeValue.S("USER#${userId}"),
            "SK" to AttributeValue.S("ORDER#${orderDetail.orderId}"),
            "orderStatus" to orderDetail.orderStatus.let { AttributeValue.S(it) },
            "deliveryStatus" to orderDetail.deliveryStatus.let { AttributeValue.S(it) },
            "orderDate" to AttributeValue.S(dateTimeFormatter.format(LocalDateTime.now()).toString()),
            "receivedDate" to AttributeValue.S(dateTimeFormatter.format(LocalDateTime.now()).toString()),
            "orderItemDetail" to  orderDetail.orderItemDetail!!.let {  AttributeValue.L(orderItemDetailListConverter.convert(it)) },
            "totalPrice" to AttributeValue.N(orderDetail.totalPrice.toString()),
            "shippingAddress" to shippingAddressConverter.convert(orderDetail.shippingAddress!!),
            "deliveryType" to orderDetail.deliveryType.let { AttributeValue.S(it) },
        )
            println(itemValues)
        val putItemRequest = PutItemRequest {
            tableName = dynamoDbtableName
            item = itemValues
        }
            dynamoDbClient.putItem(putItemRequest)
            return getOrder(userId,orderDetail.orderId)
        }catch (e:Exception){
            println(e)
            return null
        }

    }

}