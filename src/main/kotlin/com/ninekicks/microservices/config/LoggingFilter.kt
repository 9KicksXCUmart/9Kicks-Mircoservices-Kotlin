package com.ninekicks.microservices.config

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class LoggingFilter : Filter {

    // Create logging format for incoming API request in console
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse

        val startTime = System.nanoTime()
        try {
            chain?.doFilter(request, response)
        } finally {
            val duration = System.nanoTime() - startTime
            val logEntry = "[SB] ${Instant.now()} | ${httpResponse.status} | ${formatDuration(duration)} \t | " +
                    "${request.remoteAddr} | ${httpRequest.method} \"${httpRequest.requestURI}\""
            println(logEntry)
        }
    }

    private fun formatDuration(durationNano: Long): String {
        return String.format("%.6fms", durationNano / 1_000_000.0)
    }
}