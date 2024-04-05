package com.ninekicks.microservices.service.impl

import com.ninekicks.microservices.model.User
import com.ninekicks.microservices.service.GetAuthenticationService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class GetAuthenticationServiceImpl : GetAuthenticationService {
    override fun getUserId(): String {
        val userId = (SecurityContextHolder.getContext().authentication.principal as User).userId
        return userId.replace("USER#", "")
    }
}