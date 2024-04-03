package com.ninekicks.microservices.config

import com.ninekicks.microservices.repository.impl.UserRepositoryImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val userRepository: UserRepositoryImpl,
    private val appConfig: AppConfig
) {
    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {

        httpSecurity {
            csrf {
                disable()
            }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
            authorizeRequests {
                // comment out below line for deployment
//                authorize("**", permitAll)

                //comment out below lines for development
                authorize("/api/v1/product-browsing**", permitAll)
                authorize("/api/v1/**", authenticated)
            }
            addFilterBefore<UsernamePasswordAuthenticationFilter>(JwtFilter(userRepository, appConfig))
        }

        return httpSecurity.build()
    }
}