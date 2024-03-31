package com.ninekicks.microservices.model.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ValidationResponseDTO(
    val data: Data,
    val message: String,
    val success: Boolean
) {
    data class Data(
        val email: String,
        @JsonProperty("user_id")
        val userId: String
    )
}
