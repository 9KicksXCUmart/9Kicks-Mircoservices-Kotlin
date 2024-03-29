package com.ninekicks.microservices.helper.converter

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue

class AttrToIntConverter {
    fun convert(map: Map<String,AttributeValue>?): Map<String,Int>{
        var newMap:MutableMap<String,Int> = HashMap()
        map?.forEach{(key,value)->newMap[key] = value.asN().toInt() }
        return newMap.toMap()
    }
}