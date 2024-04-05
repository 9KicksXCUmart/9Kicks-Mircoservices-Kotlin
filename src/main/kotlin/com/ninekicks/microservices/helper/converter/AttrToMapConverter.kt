package com.ninekicks.microservices.helper.converter

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue

class AttrToMapConverter {
    fun convert(map: Map<String, AttributeValue>?): Map<String,Map<String,Int>>{
        var newMap:MutableMap<String,Map<String,Int>> = HashMap()
        map?.forEach{(key,value)->newMap[key] = value.asM().mapValues{ it.value.asN().toInt()}}
        return newMap.toMap()
    }
}