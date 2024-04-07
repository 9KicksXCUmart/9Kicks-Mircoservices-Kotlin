package com.ninekicks.microservices.model

import org.springframework.security.core.GrantedAuthority

data class UserPrincipal(
    val email: String,
    val authorities: List<GrantedAuthority>
)
