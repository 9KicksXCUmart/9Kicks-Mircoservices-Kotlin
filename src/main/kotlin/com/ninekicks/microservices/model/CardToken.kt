package com.ninekicks.microservices.model

data class CardToken(
     val cardNumber: String,
     val expMonth: String,
     val expYear: String,
     val cvc: String
    ){
}