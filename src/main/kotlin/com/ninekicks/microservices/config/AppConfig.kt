package com.ninekicks.microservices.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

// Set a Bean to retrieve backend url of another backend service (GO GIN)
@Configuration
class AppConfig {
    @Value("\${go.backend.url}")
    val goBackendUrl:String? = null
}