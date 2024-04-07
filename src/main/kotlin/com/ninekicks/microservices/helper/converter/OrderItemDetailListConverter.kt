package com.ninekicks.microservices.helper.converter

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter
import com.ninekicks.microservices.model.Order

class OrderItemDetailListConverter: DynamoDBTypeConverter<List<AttributeValue>, List<Order.OrderItemDetail>> {

    override fun convert(list: List<Order.OrderItemDetail>): List<AttributeValue> {
      return list.map{
            val itemMap = mutableMapOf<String, AttributeValue>()
            itemMap["productId"]= AttributeValue.S(it.productId)
            itemMap["productName"]= AttributeValue.S(it.productName)
            itemMap["productSize"]= AttributeValue.S(it.productSize)
            itemMap["productCategory"]= AttributeValue.S(it.productCategory)
            itemMap["productImage"]= AttributeValue.S(it.productImage)
            itemMap["productPrice"]= AttributeValue.N(it.productPrice.toString())
            itemMap["productQuantity"]= AttributeValue.N(it.productQuantity.toString())
            AttributeValue.M(itemMap.toMap())
        }
    }

    override fun unconvert(itemList: List<AttributeValue>?): List<Order.OrderItemDetail>? {
        return itemList?.map {
            Order.OrderItemDetail(
                productId = it.asM().getValue("productId").asS().replace("PRODUCT#", ""),
                productName = it.asM().getValue("productName").asS(),
                productSize = it.asM().getValue("productSize").asS(),
                productCategory = it.asM().getValue("productCategory").asS(),
                productImage = it.asM().getValue("productImage").asS(),
                productPrice = it.asM().getValue("productPrice").asN().toDouble(),
                productQuantity = it.asM().getValue("productQuantity").asN().toInt(),
            )
        }
    }
}