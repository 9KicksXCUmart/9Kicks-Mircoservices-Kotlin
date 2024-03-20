package com.ninekicks.microservices.service

import org.springframework.http.ResponseEntity

interface AccountSummaryService{
    fun displayUserProfile(userId:String): ResponseEntity<Any>
}