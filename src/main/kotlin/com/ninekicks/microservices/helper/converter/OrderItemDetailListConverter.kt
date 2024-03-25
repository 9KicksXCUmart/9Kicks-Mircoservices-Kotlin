package com.ninekicks.microservices.helper.converter

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter
import com.ninekicks.microservices.model.Order

class OrderItemDetailListConverter: DynamoDBTypeConverter<List<AttributeValue>, List<Order.OrderItemDetail>> {

    override fun convert(list: List<Order.OrderItemDetail>): List<AttributeValue> {
//        return AttributeValue.L(list)
        TODO()
    }

    override fun unconvert(itemList: List<AttributeValue>?): List<Order.OrderItemDetail>? {
        return itemList?.map {
            Order.OrderItemDetail(
                productId = it.asM().getValue("productId").asS().replace("PRODUCT#", ""),
                sizeQuantity = it.asM().getValue("sizeQuantity").asM().mapValues { item -> item.value.asN().toInt() }
            )
        }
    }
}