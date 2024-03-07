package com.ninekicks.microservices.repository.impl

import aws.sdk.kotlin.runtime.auth.credentials.EnvironmentCredentialsProvider
import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.ListTablesRequest
import com.ninekicks.microservices.repository.UserRepository
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl: UserRepository {

}