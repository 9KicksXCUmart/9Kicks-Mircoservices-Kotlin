package com.ninekicks.microservices.repository.impl

import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.ListTablesRequest
import com.ninekicks.microservices.repository.DemoRepository
import org.springframework.stereotype.Repository

@Repository
class DemoRepositoryImpl: DemoRepository {
    override suspend fun getAllTable(): List<String>?{
        val dynamoDbClient = DynamoDbClient.fromEnvironment { }
        val response = dynamoDbClient.listTables(ListTablesRequest { })
        return response.tableNames
    }
}