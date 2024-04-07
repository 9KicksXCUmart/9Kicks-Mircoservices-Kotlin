package com.ninekicks.microservices.model

import com.amazonaws.services.dynamodbv2.datamodeling.*
import com.ninekicks.microservices.helper.converter.ShoppingCartItemConverter

@DynamoDBTable(tableName ="9Kicks")
data class ShoppingCart(
    @DynamoDBAttribute(attributeName = "shoppingCartItemDetail")
    @DynamoDBTypeConverted(converter = ShoppingCartItemConverter::class)
    var shoppingCartItemDetail: Map<String, ItemDetail> = HashMap()
){
        data class ItemDetail(
            var productId:String = "",
            var price:Double = 0.0,
            var productQuantity:Int  = 0,
            var imageUrl:String  = "",
            var productSize:String = "",
            var productName:String = "",
            var productCategory:String = "",
        )
}