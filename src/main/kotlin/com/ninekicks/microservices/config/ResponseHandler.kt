package com.ninekicks.microservices.config

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class ResponseHandler {
    private fun generateResponse(message: String? = "", status: Boolean, responseObj: Any?): ResponseEntity<Any> {
        val map = mutableMapOf(
            "status" to status,
            "message" to message,
            "data" to responseObj
        )
        println(ResponseEntity<Any>(map, HttpStatus.OK))
        return ResponseEntity<Any>(map, HttpStatus.OK)
    }
    fun validateResponse(
        successMessage: String = "Success",
        failMessage: String = "",
        matchingObject: Any?,
        failReturnObject: Any? = null
    ): ResponseEntity<Any> {
        return try{
            if(matchingObject == null)
                generateResponse(failMessage, false, failReturnObject)
            else
                generateResponse(successMessage, true, matchingObject)
        } catch (e: Exception) {
            generateResponse(e.message, false, failReturnObject)
        }
    }
}