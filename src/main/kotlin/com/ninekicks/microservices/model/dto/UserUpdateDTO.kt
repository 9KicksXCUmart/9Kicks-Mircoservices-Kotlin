package com.ninekicks.microservices.model.dto

import com.ninekicks.microservices.model.User

data class UserUpdateDTO(
    var userId: String = "USER#",
    var password: String?,
    var firstName: String?,
    var lastName: String?,
    var email: String?,
    var shippingAddress: User.ShippingAddress?,
    var isVerified: Boolean?,
) {
}
