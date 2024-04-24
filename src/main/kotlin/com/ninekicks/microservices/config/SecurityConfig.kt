package com.ninekicks.microservices.config

import com.ninekicks.microservices.repository.impl.UserRepositoryImpl
import jakarta.servlet.Filter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

// A class for handling all the filters of outgoing APIs
// Including allowing request from authorized users
// and blocking request from unauthorized users
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
            // managing permission incoming request
            authorizeRequests {
                // comment out below line for deployment
//                authorize("**", permitAll)

                //comment out below lines for development
                authorize("/api/v1/product-browsing**", permitAll)  // user does not require to log in to perform product browsing
                authorize("/api/v1/**", authenticated)              // other APIs require logging in
            }
            addFilterBefore<UsernamePasswordAuthenticationFilter>(JwtFilter(userRepository, appConfig))
            addFilterBefore<JwtFilter>(LoggingFilter())     // apply logging filter for all outgoing requests
        }

        return httpSecurity.build()
    }
}