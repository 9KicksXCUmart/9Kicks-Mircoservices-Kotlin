package com.ninekicks.microservices.repository.impl

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.GetItemRequest
import com.ninekicks.microservices.config.DynamoDBConfig

import com.ninekicks.microservices.helper.converter.ImageUrlConverter
import com.ninekicks.microservices.helper.converter.ProductSizeConverter
import com.ninekicks.microservices.helper.converter.AttrToIntConverter
import com.ninekicks.microservices.helper.converter.DetailImageUrlConverter

import com.ninekicks.microservices.model.Product
import com.ninekicks.microservices.repository.ProductRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl(
        private val dynamoDBConfig: DynamoDBConfig
): ProductRepository {
    private val dynamoDbClient by lazy { dynamoDBConfig.dynamoDbClient() }

    @Value("\${dynamodb.tableName}")
    private val dynamoDbtableName:String? = null

    private val imageUrlConverter = ImageUrlConverter()
    private val productSizeConverter = ProductSizeConverter()
    private val attrToIntConverter = AttrToIntConverter()

    override suspend fun getProductDetail(productId: String): Product? {
        // Set the Partition Key and Sort Key for Query in DynamoDB
        val keyToGet = mutableMapOf<String, AttributeValue>(
                "PK" to AttributeValue.S("PRODUCT#$productId"),
                "SK" to AttributeValue.S("PRODUCT_DETAIL")
        )
        // construct get Item request to DynamoDB
        val itemRequest = GetItemRequest {
            key = keyToGet
            tableName = dynamoDbtableName
        }
        try {
            val returnedItem = dynamoDbClient.getItem(itemRequest)
            val itemMap: Map<String, AttributeValue> = returnedItem.item!!
            // Retrieve and map received product from DynamoDB
            return Product(
                    productId = itemMap["PK"]!!.asS(),
                    productName = itemMap["productName"]!!.asS(),
                    productBrand = itemMap["productBrand"]!!.asS(),
                    productCategory = itemMap["productCategory"]!!.asS(),
                    productSlug = itemMap["productSlug"]!!.asS(),
                    imageUrl = itemMap["imageUrl"]!!.asS(),
                    price = itemMap["price"]!!.asN().toDouble(),
                    isDiscount = itemMap["isDiscount"]!!.asBool(),
                    discountPrice = itemMap["discountPrice"]?.asN()?.toDouble(),
                    detailImageUrl = imageUrlConverter.unconvert(itemMap["detailImageUrl"])!!.map{it.asS()},
                    productSize = attrToIntConverter.convert(productSizeConverter.unconvert(itemMap["productSize"]))
            )

        }catch(e: Exception) {
            println(e)
        }
        return null
    }

}