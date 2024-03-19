package com.ninekicks.microservices.helper

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter

class ProductSizeConverter : DynamoDBTypeConverter<AttributeValue, List<AttributeValue>> {
    override fun convert(list: List<AttributeValue>): AttributeValue {
        return AttributeValue.L(list)
    }

    override fun unconvert(itemList: AttributeValue?): List<AttributeValue> {
        return itemList?.asL()?:emptyList()
    }
}