package com.ninekicks.microservices.helper.converter

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter

class ImageUrlConverter : DynamoDBTypeConverter<AttributeValue, List<AttributeValue>> {
    override fun convert(list: List<AttributeValue>): AttributeValue {
        return AttributeValue.L(list)
    }

    override fun unconvert(itemList: AttributeValue?): List<AttributeValue> {
        return itemList?.asL()?:emptyList()
    }
}