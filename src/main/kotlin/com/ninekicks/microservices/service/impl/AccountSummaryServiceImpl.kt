package com.ninekicks.microservices.service.impl

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import com.ninekicks.microservices.config.ResponseHandler
import com.ninekicks.microservices.model.Order
import com.ninekicks.microservices.model.dto.OrderDetailDTO
import com.ninekicks.microservices.model.dto.UserUpdateDTO
import com.ninekicks.microservices.repository.impl.OrderRepositoryImpl
import com.ninekicks.microservices.repository.impl.UserRepositoryImpl
import com.ninekicks.microservices.service.AccountSummaryService
import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class AccountSummaryServiceImpl(
    private val userRepository: UserRepositoryImpl,
    private val orderRepository: OrderRepositoryImpl,
) : AccountSummaryService {
    private val responseHandler = ResponseHandler()

    // Get User Details by User ID
    override fun displayUserDetails(userId: String): ResponseEntity<Any> {
        return runBlocking {
            val user = userRepository.getUser(userId)
            responseHandler.validateResponse(
                failMessage = "User not found",
                matchingObject = user,
                failReturnObject = null
            )
        }
    }

    // List all the Orders of a particular User with User ID
    // Can select Page Size and create pagination with Last Order ID
    override fun listOrdersByUserId(
        userId: String,
        pageSize: Int,
        lastOrderKey: String?
    ): ResponseEntity<Any> {
        return runBlocking {
            // check if Last Order ID exist, if yes, it will be applied to the Query
            val lastEvaluatedKey = lastOrderKey?.let {
                mapOf(
                    "PK" to AttributeValue.S("USER#$userId"),
                    "SK" to AttributeValue.S("ORDER#$it")
                )
            }

            val orders = orderRepository.getOrdersByUserId(userId, pageSize, lastEvaluatedKey)
            val orderDetailDtoList = orders?.map {
                OrderDetailDTO(
                    userId = it.userId,
                    orderId = it.orderId,
                    orderStatus = it.orderStatus,
                    deliveryStatus = it.deliveryStatus,
                    orderDate = it.orderDate,
                    receivedDate = it.receivedDate,
                    orderItemDetail = it.orderItemDetail,
                    totalPrice = it.totalPrice,
                    shippingAddress = it.shippingAddress,
                    deliveryType = it.deliveryType
                )
            }
            responseHandler.validateResponse(
                failMessage = "No orders found",
                matchingObject = orderDetailDtoList,
                failReturnObject = emptyList<Order>()
            )
        }
    }

    // Display All Order Details by User ID and Order ID
    override fun displayOrderDetails(userId: String, orderId: String): ResponseEntity<Any> {
        return runBlocking {
            val order = orderRepository.getOrder(userId, orderId)
            val orderDetail = OrderDetailDTO(
                userId = order!!.userId,
                orderId = order.orderId,
                orderStatus = order.orderStatus,
                deliveryStatus = order.deliveryStatus,
                orderDate = order.orderDate,
                receivedDate = order.receivedDate,
                orderItemDetail = order.orderItemDetail,
                totalPrice = order.totalPrice,
                shippingAddress = order.shippingAddress,
                deliveryType = order.deliveryType

            )
            responseHandler.validateResponse(
                failMessage = "No orders found",
                matchingObject = orderDetail,
                failReturnObject = null
            )
        }
    }

    // Update User Details by passing in User ID and the corresponding User Update DTO
    override fun updateUserDetails(userId: String, userUpdateDTO: UserUpdateDTO): ResponseEntity<Any> {
        return runBlocking {
            val user = userRepository.updateUser(userId, userUpdateDTO)
            responseHandler.validateResponse(
                failMessage = "No user found",
                matchingObject = user,
                failReturnObject = null
            )
        }
    }
}