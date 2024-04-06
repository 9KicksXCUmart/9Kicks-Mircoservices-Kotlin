package com.ninekicks.microservices.model.dto

import com.fasterxml.jackson.annotation.JsonProperty
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

data class ProductResponseDTO(
    val data: Data,
    val message: String,
    val success: Boolean
){
    data class Data(
        val id: String,
        val brand: String,
        val name: String,
        val category: String,
        val price: Double,
        val discountPrice: Double,
        val isDiscount: Boolean,
        val sku: String,
        val imageUrl: String,
        val detailImageUrl:List<String>,
        val publishDate:String,
        val size: Map<String, Int>,
        val reviewIdList:List<String>,
        val buyCount:Int
    )
}