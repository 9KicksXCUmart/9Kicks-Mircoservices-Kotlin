package com.ninekicks.microservices.repository.impl

import aws.sdk.kotlin.services.dynamodb.model.*
import com.ninekicks.microservices.config.DynamoDBConfig
import com.ninekicks.microservices.helper.converter.AttrToIntConverter
import com.ninekicks.microservices.helper.converter.CreditCardConverter
import com.ninekicks.microservices.helper.converter.ShippingAddressConverter
import com.ninekicks.microservices.helper.converter.ShoppingCartItemConverter
import com.ninekicks.microservices.model.ShoppingCart
import com.ninekicks.microservices.model.User
import com.ninekicks.microservices.model.dto.ShoppingCartUpdateDTO
import com.ninekicks.microservices.model.dto.UserUpdateDTO
import com.ninekicks.microservices.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.util.*
import kotlin.collections.HashMap


@Repository
class UserRepositoryImpl(
    private val dynamoDBConfig: DynamoDBConfig
) : UserRepository {
    private val dynamoDbClient by lazy { dynamoDBConfig.dynamoDbClient() }

    @Value("\${dynamodb.tableName}")
    private val dynamoDbtableName: String? = null

    private val creditCardConverter = CreditCardConverter()
    private val shippingAddressConverter = ShippingAddressConverter()
    private val attrToIntConverter = AttrToIntConverter()
    private val shoppingCartItemConverter = ShoppingCartItemConverter()


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
                shippingAddress = shippingAddressConverter.unconvert(itemMap["shippingAddress"]),
                verificationToken = itemMap["verificationToken"]?.asS(),
                tokenExpiry = itemMap["tokenExpiry"]?.asN()?.toInt()
            )
        } catch (e: Exception) {
            println(e)
        }
        return null
    }

    override suspend fun getUserByEmail(email: String): User? {

        val queryRequest = QueryRequest {
            this.tableName = dynamoDbtableName
            this.indexName = "User-email-index"
//            this.indexName = "email-PK-index"
            this.keyConditionExpression = "email = :email"
            this.expressionAttributeValues = mapOf(":email" to AttributeValue.S(email))
        }

        try {
            val queryResult = dynamoDbClient.query(queryRequest)
            val itemMap = queryResult.items!![0]

            return User(
                userId = itemMap["PK"]!!.asS(),
                email = itemMap["email"]!!.asS(),
                firstName = itemMap["firstName"]!!.asS(),
                lastName = itemMap["lastName"]!!.asS(),
                password = itemMap["password"]!!.asS(),
                creditCardDetails = creditCardConverter.unconvert(itemMap["creditCardDetails"]),
                isVerified = itemMap["isVerified"]!!.asBool(),
                shippingAddress = shippingAddressConverter.unconvert(itemMap["shippingAddress"]),
                verificationToken = itemMap["verificationToken"]?.asS(),
                tokenExpiry = itemMap["tokenExpiry"]?.asN()?.toInt()
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

        val queryRequest = QueryRequest {
            this.tableName = dynamoDbtableName
            this.indexName = "SK-PK-index"
            this.keyConditionExpression = "SK = :sk"
            this.expressionAttributeValues = mapOf(":sk" to AttributeValue.S("USER_PROFILE"))
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
                shippingAddress = shippingAddressConverter.unconvert(itemMap["shippingAddress"]),
                verificationToken = itemMap["verificationToken"]?.asS(),
                tokenExpiry = itemMap["tokenExpiry"]?.asN()?.toInt()
            )
        } ?: listOf()
    }

    override suspend fun addUser(userUpdateDto: UserUpdateDTO): User? {
        val userId = UUID.randomUUID().toString()
        val itemValues = mapOf(
            "PK" to AttributeValue.S("USER#$userId"),
            "SK" to AttributeValue.S("USER_PROFILE"),
            "email" to AttributeValue.S(userUpdateDto.email!!),
            "password" to AttributeValue.S(userUpdateDto.password!!),
            "firstName" to AttributeValue.S(userUpdateDto.firstName!!),
            "lastName" to AttributeValue.S(userUpdateDto.lastName!!),
            "shippingAddress" to shippingAddressConverter.convert(userUpdateDto.shippingAddress!!),
            "isVerified" to AttributeValue.Bool(userUpdateDto.isVerified!!),
            "verificationToken" to AttributeValue.S(""),
            "tokenExpiry" to AttributeValue.N("0")
        )

        val putItemRequest = PutItemRequest {
            tableName = dynamoDbtableName
            item = itemValues
        }
        dynamoDbClient.putItem(putItemRequest)
        return getUser(userId)
    }

    override suspend fun updateUser(userId: String, userUpdateDto: UserUpdateDTO): User? {
        keyToGet["PK"] = AttributeValue.S("USER#${userId}")
//        keyToGet["PK"] = AttributeValue.S("USER#120499e3-fdfd-440c-1204-bdcd954f4891")

        val shippingAddressConverter = ShippingAddressConverter()
        val attributeUpdates = mapOf(
            "email" to userUpdateDto.email?.let { AttributeValue.S(it) },
            "password" to userUpdateDto.password?.let { AttributeValue.S(it) },
            "firstName" to userUpdateDto.firstName?.let { AttributeValue.S(it) },
            "lastName" to userUpdateDto.lastName?.let { AttributeValue.S(it) },
            "shippingAddress" to userUpdateDto.shippingAddress?.let { shippingAddressConverter.convert(it) },
            "isVerified" to userUpdateDto.isVerified?.let { AttributeValue.Bool(it) }
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
        return getUser(userId)
    }

    override suspend fun deleteUser(userId: String): Boolean {
        keyToGet["PK"] = AttributeValue.S("USER#$userId")

        val deleteItemRequest = DeleteItemRequest {
            tableName = dynamoDbtableName
            key = keyToGet
        }
        println(deleteItemRequest)
        return try {
            dynamoDbClient.deleteItem(deleteItemRequest)
            getUser(userId) != null
        } catch (e: DynamoDbException) {
            println(e.message)
            false
        }
    }

    override suspend fun getShoppingCartDeatil(userId: String): ShoppingCart? {
        keyToGet["PK"] = AttributeValue.S("USER#$userId")
        val itemRequest = GetItemRequest {
            key = keyToGet
            tableName = dynamoDbtableName
        }
        try {
            val returnedItem = dynamoDbClient.getItem(itemRequest)
            val itemMap: Map<String, AttributeValue> = returnedItem.item!!
            return ShoppingCart(
                shoppingCartItemDetail = shoppingCartItemConverter.unconvert(itemMap["shoppingCartItemDetail"])!!
            )
        } catch (e: DynamoDbException) {
            println(e)
        }
        return null
    }

    override suspend fun updateShoppingCartDeatil(shoppingCartUpdateDTO: ShoppingCartUpdateDTO,userId: String): Boolean {
        keyToGet["PK"] = AttributeValue.S("USER#${userId}")
        var shoppingCart: ShoppingCart?= getShoppingCartDeatil(userId)
        var originDetail = shoppingCart?.shoppingCartItemDetail?.toMutableMap()
        originDetail?.put(UUID.randomUUID().toString(), ShoppingCart.ItemDetail(
            productId = shoppingCartUpdateDTO.productId,
            price= shoppingCartUpdateDTO.price,
            productQuantity= shoppingCartUpdateDTO.productQuantity,
            imageUrl= shoppingCartUpdateDTO.imageUrl,
            productSize= shoppingCartUpdateDTO.productSize,
            productName= shoppingCartUpdateDTO.productName,
            productCategory= shoppingCartUpdateDTO.productCategory))
        val attributeUpdates = mapOf( "shoppingCartItemDetail" to  shoppingCartItemConverter.convert(originDetail!!) ).mapNotNull { (key, value) -> value?.let { key to it } }.toMap()
        val updateExpression = "SET " + attributeUpdates.keys.joinToString(", ") { "$it = :$it" }
        val expressionAttributeValues = attributeUpdates.entries.associate { (key, value) -> ":$key" to value }
        try{
            val updateItemRequest = UpdateItemRequest {
                this.tableName = dynamoDbtableName
                this.key = keyToGet
                this.updateExpression = updateExpression
                this.expressionAttributeValues = expressionAttributeValues
            }
            dynamoDbClient.updateItem(updateItemRequest)
            return true
        }catch (e:Exception){
            println(e)
            return false
        }
    }

    override suspend fun deleteShoppingCartItem(userId: String, itemId: String): Boolean {
        keyToGet["PK"] = AttributeValue.S("USER#$userId")
        var shoppingCart: ShoppingCart?= getShoppingCartDeatil(userId)
        if(shoppingCart?.shoppingCartItemDetail?.get(itemId)==null||shoppingCart==null) return true
        var removeItem = shoppingCart?.shoppingCartItemDetail?.toMutableMap()
        removeItem?.remove(itemId)
        val attributeUpdates = mapOf( "shoppingCartItemDetail" to  shoppingCartItemConverter.convert(removeItem!!) ).mapNotNull { (key, value) -> value?.let { key to it } }.toMap()
        val updateExpression = "SET " + attributeUpdates.keys.joinToString(", ") { "$it = :$it" }
        val expressionAttributeValues = attributeUpdates.entries.associate { (key, value) -> ":$key" to value }
        try{
            val updateItemRequest = UpdateItemRequest {
                this.tableName = dynamoDbtableName
                this.key = keyToGet
                this.updateExpression = updateExpression
                this.expressionAttributeValues = expressionAttributeValues
            }
            dynamoDbClient.updateItem(updateItemRequest)
            return true
        }catch (e:Exception){
            println(e)
            return false
         }
        }

    override suspend fun clearShoppingCartItems(userId: String): Boolean {
        keyToGet["PK"] = AttributeValue.S("USER#$userId")
        var clearItem:Map<String, ShoppingCart.ItemDetail> = HashMap()
        val attributeUpdates = mapOf( "shoppingCartItemDetail" to  shoppingCartItemConverter.convert(clearItem!!) ).mapNotNull { (key, value) -> value?.let { key to it } }.toMap()
        val updateExpression = "SET " + attributeUpdates.keys.joinToString(", ") { "$it = :$it" }
        val expressionAttributeValues = attributeUpdates.entries.associate { (key, value) -> ":$key" to value }
        try{
            val updateItemRequest = UpdateItemRequest {
                this.tableName = dynamoDbtableName
                this.key = keyToGet
                this.updateExpression = updateExpression
                this.expressionAttributeValues = expressionAttributeValues
            }
            dynamoDbClient.updateItem(updateItemRequest)
            return true
        }catch (e:Exception){
            println(e)
            return false
        }
    }
}
