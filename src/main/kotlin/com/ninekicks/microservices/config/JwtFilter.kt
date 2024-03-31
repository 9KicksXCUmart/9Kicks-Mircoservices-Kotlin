package com.ninekicks.microservices.config

import com.ninekicks.microservices.model.dto.ValidationResponseDTO
import com.ninekicks.microservices.repository.impl.UserRepositoryImpl
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.filter.OncePerRequestFilter


@Component
class JwtFilter(
    private val userRepository: UserRepositoryImpl,
    private val appConfig: AppConfig
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val bearerToken = request.getHeader("Authorization")
        if (bearerToken.isNullOrEmpty()) {
            filterChain.doFilter(request, response)
            return
        }

        if (!bearerToken.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val token = bearerToken.substring(7)
        if (token.isBlank()) {
            filterChain.doFilter(request, response)
            return
        }

        try {
            val validationResponseDto = verifyJwtToken(token)

            if (validationResponseDto!!.success) {
                val userDetails = runBlocking { userRepository.getUser(validationResponseDto.data.userId) }
                val authToken = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    listOf()
                )
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authToken
            }
        } catch (e: Exception) {
            println(e.message)
        } finally {
            filterChain.doFilter(request, response)
        }
    }

    private fun verifyJwtToken(jwt: String): ValidationResponseDTO? {
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer $jwt")

        val entity = HttpEntity<String>("", headers)
        val response = restTemplate.exchange(
            "${appConfig.goBackendUrl}/v1/auth/validate-token",
            HttpMethod.POST,
            entity,
            ValidationResponseDTO::class.java
        )
        return response.body
    }
}