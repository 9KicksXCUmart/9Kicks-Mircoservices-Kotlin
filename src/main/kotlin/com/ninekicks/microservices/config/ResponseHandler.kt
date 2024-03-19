package com.ninekicks.microservices.config

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class ResponseHandler {
    fun generateResponse(message: String? = "", status: Boolean, responseObj: Any?): ResponseEntity<Any> {
        val map = mutableMapOf(
            "status" to status,
            "message" to message,
            "data" to responseObj
        )
        println(ResponseEntity<Any>(map, HttpStatus.OK))
        return ResponseEntity<Any>(map, HttpStatus.OK)
    }
}