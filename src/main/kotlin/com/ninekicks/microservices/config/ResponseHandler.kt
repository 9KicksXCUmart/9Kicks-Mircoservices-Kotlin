package com.ninekicks.microservices.config

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

// A class for handling all outgoing APIs response
// Included try-catch-finally to catch potential errors
class ResponseHandler {
    private fun generateResponse(message: String? = "", status: Boolean, responseObj: Any?): ResponseEntity<Any> {
        val map = mutableMapOf(
            "status" to status,     // true(success) or false(failure)
            "message" to message,   // success or failure message
            "data" to responseObj   // object to return to outgoing API response
        )
        println(ResponseEntity<Any>(map, HttpStatus.OK))
        // the outgoing response will have 3 elements: status, message, data
        return ResponseEntity<Any>(map, HttpStatus.OK)
    }
    // validate if the returned object from CRUD matches the desire object type/value
    fun validateResponse(
        successMessage: String = "Success",
        failMessage: String = "",
        matchingObject: Any?,
        failReturnObject: Any? = null // object to return if it does not match the desire object from CRUD
    ): ResponseEntity<Any> {
        return try{
            if(matchingObject == null)  // if object from CRUD is null, generate a failed response to outgoing API
                generateResponse(failMessage, false, failReturnObject)
            else                        // otherwise, generate a response with the corresponding object from CRUD
                generateResponse(successMessage, true, matchingObject)
        } catch (e: Exception) {
            generateResponse(e.message, false, failReturnObject)
        }
    }
}