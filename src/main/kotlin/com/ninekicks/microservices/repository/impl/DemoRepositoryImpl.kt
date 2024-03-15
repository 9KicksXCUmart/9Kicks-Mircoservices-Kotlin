package com.ninekicks.microservices.repository.impl

import aws.sdk.kotlin.services.dynamodb.model.ListTablesRequest
import com.ninekicks.microservices.config.DynamoDBConfig
import com.ninekicks.microservices.repository.DemoRepository
import org.springframework.stereotype.Repository

@Repository
class DemoRepositoryImpl(
    private val dynamoDBConfig: DynamoDBConfig
): DemoRepository {

    private val dynamoDbClient by lazy { dynamoDBConfig.dynamoDbClient() }
    override suspend fun getAllTable(): List<String>?{
        val response = dynamoDbClient.listTables(ListTablesRequest { })
        return response.tableNames
    }
}