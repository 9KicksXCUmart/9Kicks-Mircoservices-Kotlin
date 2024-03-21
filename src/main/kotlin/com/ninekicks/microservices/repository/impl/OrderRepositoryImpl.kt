package com.ninekicks.microservices.repository.impl

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.QueryRequest
import com.ninekicks.microservices.config.DynamoDBConfig
import com.ninekicks.microservices.helper.converter.OrderItemDetailListConverter
import com.ninekicks.microservices.model.Order
import com.ninekicks.microservices.model.enum.DeliveryStatus
import com.ninekicks.microservices.model.enum.OrderStatus
import com.ninekicks.microservices.repository.OrderRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Repository
class OrderRepositoryImpl(
    private val dynamoDBConfig: DynamoDBConfig
): OrderRepository {
    private val dynamoDbClient by lazy { dynamoDBConfig.dynamoDbClient() }

    @Value("\${dynamodb.tableName}")
    private val dynamoDbtableName:String? = null
    private val orderItemDetailListConverter = OrderItemDetailListConverter()

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("d/M/yyyy H:mm")

    private val keyToGet = mutableMapOf<String, AttributeValue>(
        "PK" to AttributeValue.S("USER#"),
        "SK" to AttributeValue.S("ORDER#")
    )

    override suspend fun getOrdersByUserId(
        userId: String,
        pageSize: Int,
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
        return queryResult.items?.map { itemMap ->
            Order(
                userId= itemMap["PK"]!!.asS(),
                orderId = itemMap["SK"]!!.asS(),
                orderStatus = enumValueOf<OrderStatus>(itemMap["orderStatus"]!!.asS()),
                deliveryStatus = enumValueOf<DeliveryStatus>(itemMap["deliveryStatus"]!!.asS()),
                orderDate = LocalDateTime.parse(itemMap["orderDate"]!!.asS(), dateTimeFormatter),
                receivedDate = itemMap["receivedDate"]?.asS()?.takeIf { it.isNotBlank() }?.let { LocalDateTime.parse(it,dateTimeFormatter) },
                orderItemDetail = orderItemDetailListConverter.unconvert(itemMap["orderItemDetail"]?.asL())!!,
                totalPrice = itemMap["totalPrice"]!!.asN().toFloat()
            )
        }
    }
}