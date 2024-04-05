package com.ninekicks.microservices.model.dto

class ShoppingCartUpdateDTO(
    var userId:String,
    var productId:String,
    var price:Double,
    var productQuantity:Int,
    var imageUrl:String,
    var productSize:String,
    var productName:String,
    var productCategory:String,
){

}