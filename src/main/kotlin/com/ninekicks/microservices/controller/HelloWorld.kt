package com.ninekicks.microservices.controller

import com.ninekicks.microservices.repository.UserRepository
import com.ninekicks.microservices.repository.impl.UserRepositoryImpl
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class HelloWorld {
    @GetMapping("/helloworld")
    fun helloWorld():String {
        return "helloworld"
    }

    lateinit var userRepository: UserRepositoryImpl
    @GetMapping("/testing")
    fun testing(){
        suspend { userRepository.getAllTable() }
    }
}


