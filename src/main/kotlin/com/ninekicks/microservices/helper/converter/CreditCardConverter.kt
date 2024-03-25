package com.ninekicks.microservices.helper.converter

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter
import com.ninekicks.microservices.model.User

class CreditCardConverter : DynamoDBTypeConverter<AttributeValue, User.CreditCardDetails> {
    override fun convert(creditCardDetails: User.CreditCardDetails): AttributeValue {
        val itemMap = mutableMapOf<String, AttributeValue>()
        itemMap["creditCardNumber"] = AttributeValue.N(creditCardDetails.cardNumber.toString())
        itemMap["creditCardMM"] = AttributeValue.N(creditCardDetails.mm.toString())
        itemMap["creditCardYY"] = AttributeValue.N(creditCardDetails.yy.toString())
        itemMap["creditCardCVV"] = AttributeValue.N(creditCardDetails.cvv.toString())
        return AttributeValue.M(itemMap)
    }

    override fun unconvert(itemMap: AttributeValue?): User.CreditCardDetails {
        try {
            itemMap?.asM()?.let { map ->
                return User.CreditCardDetails(
                    cardNumber = map["creditCardNumber"]?.asN()?.toLong() ?: 0,
                    mm = map["creditCardMM"]?.asN()?.toInt() ?: 0,
                    yy = map["creditCardYY"]?.asN()?.toInt() ?: 0,
                    cvv = map["creditCardCVV"]?.asN()?.toInt() ?: 0,
                )
            }
        } catch (e: Exception){
            println(e)
        }
        return User.CreditCardDetails()
    }
}