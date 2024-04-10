package com.ninekicks.microservices.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebServerConfiguration {

    @Value("\${cors.originPatterns:default}")
    private val corsOriginPatterns: String = ""

    @Bean
    fun addCorsConfig(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                var allowedOrigins = corsOriginPatterns.split(",").toTypedArray()
                allowedOrigins += "http://localhost:5173"
                allowedOrigins += "https://9kicks.shop"
                allowedOrigins += "https://www.9kicks.shop"
                allowedOrigins += "https://9-kicks-shop.vercel.app"
                allowedOrigins += "https://www.admin.9kicks.shop"
                allowedOrigins += "https://admin.9kicks.shop"
                registry.addMapping("/**")
                    .allowedMethods("*")
                    .allowedOriginPatterns(*allowedOrigins)
                    .allowCredentials(true)
            }
        }
    }
}
