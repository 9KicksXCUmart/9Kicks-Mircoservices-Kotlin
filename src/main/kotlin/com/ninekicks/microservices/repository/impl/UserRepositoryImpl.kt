package com.ninekicks.microservices.repository.impl

import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.ListTablesRequest
import com.ninekicks.microservices.repository.UserRepository

class UserRepositoryImpl: UserRepository {

    override suspend fun getAllTable(){
        DynamoDbClient { region = "ap-southeast-1" }.use { ddb ->
            val response = ddb.listTables(ListTablesRequest {})
            response.tableNames?.forEach { tableName ->
                println("Table name is $tableName")
            }
        }
    }
}