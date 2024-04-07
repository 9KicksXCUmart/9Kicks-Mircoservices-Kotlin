package com.ninekicks.microservices.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {
    @Value("\${go.backend.url}")
    val goBackendUrl:String? = null
}