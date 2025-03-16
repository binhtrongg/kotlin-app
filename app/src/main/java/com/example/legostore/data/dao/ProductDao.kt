package com.example.legostore.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.legostore.data.entity.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAllProducts(): LiveData<List<Product>>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: Long): Product?

    @Insert
    suspend fun insertProduct(product: Product): Long

    @Insert
    suspend fun insertProducts(products: List<Product>)

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)
} 