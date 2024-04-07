package com.ninekicks.microservices.helper.converter

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter
import com.ninekicks.microservices.model.User

class ShippingAddressConverter: DynamoDBTypeConverter<AttributeValue, User.ShippingAddress> {
    override fun convert(shippingAddress: User.ShippingAddress?): AttributeValue {
        val itemMap = mutableMapOf<String, AttributeValue>()
        shippingAddress?.let {
            itemMap["streetAddress"] = AttributeValue.S(shippingAddress.streetAddress)
            itemMap["district"] = AttributeValue.S(shippingAddress.district)
        }
        return AttributeValue.M(itemMap)
    }

    override fun unconvert(itemMap: AttributeValue?): User.ShippingAddress {
        try {
            itemMap?.asM()?.let { map ->
                return User.ShippingAddress(
                    streetAddress = map["streetAddress"]?.asS()?: "",
                    district = map["district"]?.asS()?: "",
                )
            } ?: User.ShippingAddress()
        } catch (e: Exception){
            println(e)
        }
        return User.ShippingAddress()
    }
}