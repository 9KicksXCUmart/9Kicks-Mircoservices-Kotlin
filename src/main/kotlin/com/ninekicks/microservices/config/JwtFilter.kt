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

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val bearerToken = request.getHeader("Authorization")

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            val token = bearerToken.substring(7)
            if (verifyJwtToken(token)!!.isNotEmpty()) {
                val userDetails = runBlocking {  userRepository.getUser("98ea39e3-fdfd-440c-b931-bdcd954f4891") }
                val authToken = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    listOf()
                )
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authToken
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun verifyJwtToken(jwt: String): String? {
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer $jwt")

        val entity = HttpEntity<String>("", headers)
        val response = restTemplate.exchange(
            "${appConfig.goBackendUrl}/validate",
            HttpMethod.GET,
            entity,
            ValidationResponseDTO::class.java
        )
        println("response: ${response.body?.email}")
        return response.body?.email
    }
}