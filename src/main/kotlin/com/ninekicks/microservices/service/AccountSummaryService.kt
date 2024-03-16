package com.ninekicks.microservices.service

import com.ninekicks.microservices.model.User

interface AccountSummaryService{
    fun displayUserProfile(userId:String): User?
}