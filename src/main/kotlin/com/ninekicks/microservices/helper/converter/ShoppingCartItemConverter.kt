package com.ninekicks.microservices.helper.converter

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter
import com.ninekicks.microservices.model.ShoppingCart



class ShoppingCartItemConverter : DynamoDBTypeConverter<AttributeValue, Map<String, ShoppingCart.ItemDetail>?> {

    override fun convert(shoppingCartItemDetail: Map<String, ShoppingCart.ItemDetail>?): AttributeValue {
        val itemMap = mutableMapOf<String, AttributeValue>()
        shoppingCartItemDetail?.forEach{
            var detailMap = mutableMapOf<String, AttributeValue>()
            detailMap["productId"]= AttributeValue.S(it.value.productId)
            detailMap["price"]= AttributeValue.N(it.value.price.toString())
            detailMap["productQuantity"]= AttributeValue.N(it.value.productQuantity.toString())
            detailMap["imageUrl"]= AttributeValue.S(it.value.imageUrl)
            detailMap["productSize"]= AttributeValue.S(it.value.productSize)
            detailMap["originalPrice"]= AttributeValue.N(it.value.originalPrice.toString())
            detailMap["productName"]= AttributeValue.S(it.value.productName)
            detailMap["productCategory"]= AttributeValue.S(it.value.productCategory)
            itemMap[it.key] = AttributeValue.M(detailMap)
        }
        return AttributeValue.M(itemMap)
    }

    override fun unconvert(itemMap: AttributeValue?):Map<String, ShoppingCart.ItemDetail>? {
        var itemDetailMap:MutableMap<String,ShoppingCart.ItemDetail>? = HashMap()
        itemMap?.asM()?.forEach {
            var itemDetail:ShoppingCart.ItemDetail?
            it.value.asM().let { map ->
                itemDetail = ShoppingCart.ItemDetail(
                   productId = map["productId"]?.asS()?:"",
                    price  = map["price"]?.asN()?.toDouble()?:0.0,
                    productQuantity=map["productQuantity"]?.asN()?.toInt()?:0,
                    imageUrl = map["imageUrl"]?.asS()?:"",
                    productSize = map["productSize"]?.asS()?:"",
                    productName= map["productName"]?.asS()?:"",
                    productCategory= map["productCategory"]?.asS()?:"",
                    originalPrice= map["originalPrice"]?.asN()?.toDouble()?:0.0,
                )
                itemDetailMap?.put(it.key, itemDetail!!)
            }
        }
        return itemDetailMap?.toMap()
    }
}