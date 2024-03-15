package com.ninekicks.microservices.config

import aws.sdk.kotlin.runtime.auth.credentials.EnvironmentCredentialsProvider
import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope

@Configuration
class DynamoDBConfig {
    @Value("\${cloud.aws.credentials.accessKey}")
    private val accessKey: String? = null
    @Value("\${cloud.aws.credentials.secretAccessKey}")
    private val secretKey: String? = null
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    fun dynamoDbClient(): DynamoDbClient {
        return DynamoDbClient {
            region = "ap-southeast-1"
            credentialsProvider = if(accessKey.isNullOrEmpty() || secretKey.isNullOrEmpty()) EnvironmentCredentialsProvider() else StaticCredentialsProvider {
                accessKeyId = accessKey
                secretAccessKey = secretKey
            }
        }
    }
}
