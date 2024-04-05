package com.ninekicks.microservices.model

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import com.amazonaws.services.dynamodbv2.datamodeling.*
import com.ninekicks.microservices.helper.ListConverter
import com.ninekicks.microservices.helper.MapConverter

@DynamoDBTable(tableName ="9Kicks")
data class Product(
        @DynamoDBHashKey(attributeName = "PK")
        var productId:String,
        @DynamoDBRangeKey(attributeName = "SK")
        private val sortKey:String = "PRODUCT_DETAIL",
        @DynamoDBAttribute(attributeName = "productName")
        var productName:String,
        @DynamoDBAttribute(attributeName = "productBrand")
        var productBrand:String,
        @DynamoDBAttribute(attributeName = "productCategory")
        var productCategory:String,
        @DynamoDBAttribute(attributeName = "productSlug")
        var productSlug:String,
        @DynamoDBAttribute(attributeName = "imageUrl")
        var imageUrl:String,
        @DynamoDBAttribute(attributeName = "price")
        var price: Double,
        @DynamoDBAttribute(attributeName = "isDiscount")
        var isDiscount:Boolean,
        @DynamoDBAttribute(attributeName = "discountPrice")
        var discountPrice: Double?,
        @DynamoDBAttribute(attributeName = "detailImageUrl")
        @DynamoDBTypeConverted(converter = ListConverter::class)
        var detailImageUrl: List<String>,
        @DynamoDBAttribute(attributeName = "productSize")
        @DynamoDBTypeConverted(converter = MapConverter::class)
        var productSize: Map<String, Int>
) {
}




