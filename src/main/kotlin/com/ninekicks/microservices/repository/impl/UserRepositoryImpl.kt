package com.ninekicks.microservices.repository.impl

import aws.sdk.kotlin.services.dynamodb.model.*
import com.ninekicks.microservices.config.DynamoDBConfig
import com.ninekicks.microservices.helper.converter.CreditCardConverter
import com.ninekicks.microservices.helper.converter.ShippingAddressConverter
import com.ninekicks.microservices.model.User
import com.ninekicks.microservices.model.dto.UserUpdateDTO
import com.ninekicks.microservices.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val dynamoDBConfig: DynamoDBConfig
) : UserRepository {
    private val dynamoDbClient by lazy { dynamoDBConfig.dynamoDbClient() }

    @Value("\${dynamodb.tableName}")
    private val dynamoDbtableName: String? = null

    private val creditCardConverter = CreditCardConverter()
    private val shippingAddressConverter = ShippingAddressConverter()


    private val keyToGet = mutableMapOf<String, AttributeValue>(
        "PK" to AttributeValue.S("USER#"),
        "SK" to AttributeValue.S("USER_PROFILE")
    )

    override suspend fun getUser(userId: String): User? {
        keyToGet["PK"] = AttributeValue.S("USER#$userId")

        val itemRequest = GetItemRequest {
            key = keyToGet
            tableName = dynamoDbtableName
        }
        try {
            val returnedItem = dynamoDbClient.getItem(itemRequest)
            val itemMap: Map<String, AttributeValue> = returnedItem.item!!
            return User(
                userId = itemMap["PK"]!!.asS(),
                email = itemMap["email"]!!.asS(),
                firstName = itemMap["firstName"]!!.asS(),
                lastName = itemMap["lastName"]!!.asS(),
                password = itemMap["password"]!!.asS(),
                creditCardDetails = creditCardConverter.unconvert(itemMap["creditCardDetails"]),
                isVerified = itemMap["isVerified"]!!.asBool(),
                shippingAddress = shippingAddressConverter.unconvert(itemMap["shippingAddress"])
            )
        } catch (e: Exception) {
            println(e)
        }
        return null
    }

    override suspend fun getAllUsers(
        pageSize: Int,
        lastKey: Map<String, AttributeValue>?
    ): List<User> {
        val queryResponses = mutableListOf<QueryResponse>()
//        val lastEvaluatedKey: Map<String, AttributeValue>? = lastKey

        val queryRequest = QueryRequest {
            this.tableName = dynamoDbtableName
            this.indexName = "SK-PK-index"
            keyConditionExpression = "SK = :sk"
            expressionAttributeValues = mapOf(":sk" to AttributeValue.S("USER_PROFILE"))
            this.exclusiveStartKey = lastKey
            this.limit = pageSize
        }
        val queryResult = dynamoDbClient.query(queryRequest)
        queryResponses.add(queryResult)
        return queryResult.items?.map { itemMap ->
            User(
                userId = itemMap["PK"]!!.asS(),
                email = itemMap["email"]!!.asS(),
                firstName = itemMap["firstName"]!!.asS(),
                lastName = itemMap["lastName"]!!.asS(),
                password = itemMap["password"]!!.asS(),
                creditCardDetails = creditCardConverter.unconvert(itemMap["creditCardDetails"]),
                isVerified = itemMap["isVerified"]!!.asBool(),
                shippingAddress = shippingAddressConverter.unconvert(itemMap["shippingAddress"])
            )
        } ?: listOf()
    }

    override suspend fun addUser(user: User): User? {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(user: UserUpdateDTO): User? {
        keyToGet["PK"] = AttributeValue.S("USER#${user.userId}")
        keyToGet["PK"] = AttributeValue.S("USER#120499e3-fdfd-440c-1204-bdcd954f4891")

        val shippingAddressConverter = ShippingAddressConverter()

        val attributeUpdates = mapOf(
            "email" to user.email?.let { AttributeValue.S(it) },
            "password" to user.password?.let { AttributeValue.S(it) },
            "firstName" to user.firstName?.let { AttributeValue.S(it) },
            "lastName" to user.lastName?.let { AttributeValue.S(it) },
            "shippingAddress" to user.shippingAddress?.let { shippingAddressConverter.convert(it) },
            "isVerified" to user.isVerified?.let { AttributeValue.Bool(it) }
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
        return getUser(user.userId)
    }

    override suspend fun deleteUser(userId: String): Boolean? {
        TODO("Not yet implemented")
    }

}