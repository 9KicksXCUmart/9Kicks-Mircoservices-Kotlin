package com.ninekicks.microservices.repository.impl

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.GetItemRequest
import com.ninekicks.microservices.config.DynamoDBConfig
import com.ninekicks.microservices.helper.ProductSizeConverter
import com.ninekicks.microservices.helper.AttrToIntConverter
import com.ninekicks.microservices.helper.DetailImageUrlConverter
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

    private val productSizeConverter = ProductSizeConverter()
    private val detailImageUrlConverter = DetailImageUrlConverter()
    private val attrToIntConverter = AttrToIntConverter()

    override suspend fun getProductDetail(productId: String): Product? {
        val keyToGet = mutableMapOf<String, AttributeValue>(
                "PK" to AttributeValue.S("PRODUCT#$productId"),
                "SK" to AttributeValue.S("PRODUCT_DETAIL")
        )
        val itemRequest = GetItemRequest {
            key = keyToGet
            tableName = dynamoDbtableName
        }
        try {
            val returnedItem = dynamoDbClient.getItem(itemRequest)
            val itemMap: Map<String, AttributeValue> = returnedItem.item!!
            return Product(
                    productId = itemMap["PK"]!!.asS(),
                    productName = itemMap["productName"]!!.asS(),
                    productBrand = itemMap["productBrand"]!!.asS(),
                    productCategory = itemMap["productCategory"]!!.asS(),
                    productSlug = itemMap["productSlug"]!!.asS(),
                    imageUrl = itemMap["imageUrl"]!!.asS(),
                    price = itemMap["price"]!!.asN().toFloat(),
                    isDiscount = itemMap["isDiscount"]!!.asBool(),
                    discountPrice = itemMap["discountPrice"]?.asN()?.toFloat(),
                    detailImageUrl = productSizeConverter.unconvert(itemMap["detailImageUrl"])!!.map{it.asS()},
                    productSize = attrToIntConverter.convert(detailImageUrlConverter.unconvert(itemMap["productSize"]))
            )

        }catch(e: Exception) {
            println(e)
        }
        return null
    }

}