package com.ninekicks.microservices.model.dto

import com.fasterxml.jackson.annotation.JsonProperty
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

data class StockResponseDTO(
    val data: Data,
    val message: String,
    val success: Boolean
){
    data class Data(
        val remainingStock: Int,
    )
}