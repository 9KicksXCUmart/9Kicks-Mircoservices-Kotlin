package com.ninekicks.microservices.model

import com.amazonaws.services.dynamodbv2.datamodeling.*
import com.ninekicks.microservices.helper.converter.CreditCardConverter
import com.ninekicks.microservices.helper.converter.ShippingAddressConverter

@DynamoDBTable(tableName = "9Kicks")
data class User(
    @DynamoDBHashKey(attributeName = "PK")
    var userId:String,
    @DynamoDBRangeKey(attributeName = "SK")
    private val sortKey:String = "USER_PROFILE",
    @DynamoDBAttribute(attributeName = "email")
    var email:String,
    @DynamoDBAttribute(attributeName = "password")
    private var password:String,
    @DynamoDBAttribute(attributeName = "firstName")
    var firstName:String,
    @DynamoDBAttribute(attributeName = "lastName")
    var lastName:String,
    @DynamoDBAttribute(attributeName = "shippingAddress")
    @DynamoDBTypeConverted(converter = ShippingAddressConverter::class)
    var shippingAddress:ShippingAddress,
    @DynamoDBAttribute(attributeName = "isVerified")
    var isVerified:Boolean,
    @DynamoDBAttribute(attributeName = "verificationToken")
    var verificationToken:String?,
    @DynamoDBAttribute(attributeName = "tokenExpiry")
    var tokenExpiry:Int?,
    @DynamoDBAttribute(attributeName = "creditCardDetails")
    @DynamoDBTypeConverted(converter = CreditCardConverter::class)
    private var creditCardDetails:CreditCardDetails = CreditCardDetails()
) {
  data class CreditCardDetails (
      var cardNumber:Long = 0,
      var mm:Int = 0,
      var yy:Int = 0,
      var cvv:Int = 0
  )
    data class ShippingAddress (
        var streetAddress: String = "",
        var district: String = ""
    )
    fun updatePassword(newPassword:String) {
        password = newPassword
    }
    fun getPassword():String {
        return password
    }
}




