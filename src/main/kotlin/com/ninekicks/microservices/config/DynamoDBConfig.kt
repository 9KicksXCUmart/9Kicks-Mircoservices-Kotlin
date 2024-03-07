package com.ninekicks.microservices.config

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope

@Configuration
class DynamoDBConfig {
//    @Bean
//    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//    fun dynamoDbClientBuilderLocal(): DynamoDbClient.Builder {
//
//        val builder: DynamoDbClient.Builder = DynamoDbClient.builder()
//
//        builder.apply {
//            this.config.region = "ap-southeast-1"
//            // Add fake accessKey for local profile
//            this.config.credentialsProvider = StaticCredentialsProvider {
//                accessKeyId = ""
//                secretAccessKey = ""
//            }
//        }
//        return builder
//    }
}