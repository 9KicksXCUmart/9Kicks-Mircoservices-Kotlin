package com.ninekicks.microservices.service.impl

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import com.ninekicks.microservices.config.ResponseHandler
import com.ninekicks.microservices.model.Order
import com.ninekicks.microservices.model.User
import com.ninekicks.microservices.model.dto.OrderDetailDTO
import com.ninekicks.microservices.repository.impl.OrderRepositoryImpl
import com.ninekicks.microservices.repository.impl.ProductRepositoryImpl
import com.ninekicks.microservices.repository.impl.UserRepositoryImpl
import com.ninekicks.microservices.service.AccountSummaryService
import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class AccountSummaryServiceImpl(
    private val userRepository: UserRepositoryImpl,
    private val orderRepository: OrderRepositoryImpl,
    private val productRepository: ProductRepositoryImpl
): AccountSummaryService {
    private val responseHandler = ResponseHandler()
    override fun displayUserDetails(userId:String): ResponseEntity<Any> {
        return runBlocking {
            val user = userRepository.getUser(userId)
            responseHandler.validateResponse(
                failMessage = "User not found",
                matchingObject = user,
                failReturnObject = null
            )
        }
    }

    override fun listOrdersByUserId(
        userId: String,
        pageSize: Int,
        lastKey: Map<String, AttributeValue>?
    ): ResponseEntity<Any> {
        return runBlocking {
            val orders = orderRepository.getOrdersByUserId(userId, pageSize, lastKey)
            val orderDetailDtoList = orders?.map {
                OrderDetailDTO(
                    userId= it.userId,
                    orderId = it.orderId,
                        orderStatus = it.orderStatus,
                        deliveryStatus = it.deliveryStatus,
                        orderDate = it.orderDate,
                        receivedDate = it.receivedDate,
                        orderItemDetail = it.orderItemDetail?.map { item ->
                            OrderDetailDTO.OrderItemDetail(
                                product = productRepository.getProductDetail(item.productId),
                                sizeQuantity = item.sizeQuantity
                            )
                        },
                        totalPrice = it.totalPrice
                )
            }
            responseHandler.validateResponse(
                failMessage = "No orders found",
                matchingObject = orderDetailDtoList,
                failReturnObject = emptyList<Order>()
            )
        }
    }

    override fun displayOrderDetails(userId: String, orderId: String): ResponseEntity<Any> {
        TODO("Not yet implemented")
    }

    override fun updateUserDetails(user: User): ResponseEntity<Any> {
        TODO("Not yet implemented")
    }
}