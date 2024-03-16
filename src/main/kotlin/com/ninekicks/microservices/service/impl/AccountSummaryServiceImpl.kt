package com.ninekicks.microservices.service.impl

import com.ninekicks.microservices.model.User
import com.ninekicks.microservices.repository.impl.UserRepositoryImpl
import com.ninekicks.microservices.service.AccountSummaryService
import kotlinx.coroutines.runBlocking
import org.apache.catalina.connector.Response
import org.springframework.stereotype.Repository

@Repository
class AccountSummaryServiceImpl(
    private val userRepository: UserRepositoryImpl
): AccountSummaryService {
    override fun displayUserProfile(userId:String): User? {
        var user: User?
//        var userResponseDto: UserResponseDTO
        runBlocking {
            user = userRepository.getUser(userId)
//            userResponseDto = if (user == null)
//                UserResponseDTO(status = Response.SC_NOT_FOUND, message="User not found", data = null)
//            else
//                UserResponseDTO(status = Response.SC_OK, message="Successful", data = user)
        }
        return user
    }
}