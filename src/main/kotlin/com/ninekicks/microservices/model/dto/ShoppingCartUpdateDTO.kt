package com.ninekicks.microservices.model.dto

class ShoppingCartUpdateDTO(
    var productId:String,
    var price:Double,
    var productQuantity:Int,
    var imageUrl:String,
    var productSize:String,
    var productName:String,
    var productCategory:String,
){

}