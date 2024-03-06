package com.ninekicks.micoservices.repository.impl

import com.ninekicks.micoservices.repository.UserRepository

class UserRepositoryImpl: UserRepository {

    override suspend fun getAllTable(): String{
        DynamoDbClient { region = "us-east-1" }.use { ddb ->
            val response = ddb.listTables(ListTablesRequest {})
            response.tableNames?.forEach { tableName ->
                println("Table name is $tableName")
            }
        }
    }
}