package com.ninekicks.microservices.helper

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter

class MapConverter : DynamoDBTypeConverter<AttributeValue, Map<String, AttributeValue>> {
    override fun convert(map: Map<String, AttributeValue>): AttributeValue {
        return AttributeValue.M(map)
    }

    override fun unconvert(itemMap: AttributeValue?): Map<String, AttributeValue>? {
        return itemMap?.asM()
    }
}

