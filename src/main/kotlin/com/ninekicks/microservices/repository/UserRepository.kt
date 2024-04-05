package com.ninekicks.microservices.repository

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import com.ninekicks.microservices.model.ShoppingCart
import com.ninekicks.microservices.model.User
import com.ninekicks.microservices.model.dto.ShoppingCartUpdateDTO
import com.ninekicks.microservices.model.dto.UserUpdateDTO

interface UserRepository {
    suspend fun getUser(userId: String): User?
    suspend fun getUserByEmail(email: String): User?
    suspend fun getAllUsers(
        pageSize:Int,
        lastKey: Map<String, AttributeValue>?
    ): List<User>?
    suspend fun addUser(userUpdateDto: UserUpdateDTO): User?
    suspend fun updateUser(userId: String, userUpdateDto: UserUpdateDTO): User?
    suspend fun deleteUser(userId: String): Boolean
    suspend fun getShoppingCartDeatil(userId: String): ShoppingCart?
    suspend fun updateShoppingCartDeatil(shoppingCartUpdateDTO: ShoppingCartUpdateDTO): Boolean
    suspend fun deleteShoppingCartItem(userId: String, itemId:String): Boolean
    suspend fun clearShoppingCartItems(userId:String): Boolean
}