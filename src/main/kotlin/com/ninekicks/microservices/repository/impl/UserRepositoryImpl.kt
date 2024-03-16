package com.ninekicks.microservices.repository.impl

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.GetItemRequest
import com.ninekicks.microservices.config.DynamoDBConfig
import com.ninekicks.microservices.helper.CreditCardConverter
import com.ninekicks.microservices.helper.ShippingAddressConverter
import com.ninekicks.microservices.model.User
import com.ninekicks.microservices.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val dynamoDBConfig: DynamoDBConfig
): UserRepository {
    private val dynamoDbClient by lazy { dynamoDBConfig.dynamoDbClient() }

    @Value("\${dynamodb.tableName}")
    private val dynamoDbtableName:String? = null

    private val creditCardConverter = CreditCardConverter()
    private val shippingAddressConverter = ShippingAddressConverter()
    override suspend fun getUser(userId: String) : User? {

        val keyToGet = mutableMapOf<String, AttributeValue>(
            "PK" to AttributeValue.S("USER#$userId"),
            "SK" to AttributeValue.S("USER_PROFILE")
        )
        val itemRequest = GetItemRequest {
            key = keyToGet
            tableName = dynamoDbtableName
        }
        try {
            val returnedItem = dynamoDbClient.getItem(itemRequest)
            val itemMap: Map<String, AttributeValue> = returnedItem.item!!
            return User(
                userId= itemMap["PK"]!!.asS(),
                email=itemMap["email"]!!.asS(),
                firstName = itemMap["firstName"]!!.asS(),
                lastName = itemMap["lastName"]!!.asS(),
                password = itemMap["password"]!!.asS(),
                creditCardDetails=creditCardConverter.unconvert(itemMap["creditCardDetails"]),
                isVerified = itemMap["isVerified"]!!.asBool(),
                shippingAddress = shippingAddressConverter.unconvert(itemMap["shippingAddress"])
            )
        } catch (e: Exception) {
            println(e)
        }
        return null
    }

}