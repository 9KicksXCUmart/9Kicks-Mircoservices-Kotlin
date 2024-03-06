package com.ninekicks.micoservices.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/helloworld")
class HelloWorld {
    @GetMapping
    fun helloWorld():String {
        return "helloworld"
    }
}


