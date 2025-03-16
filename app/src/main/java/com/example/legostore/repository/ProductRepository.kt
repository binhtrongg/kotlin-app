package com.example.legostore.repository

import androidx.lifecycle.LiveData
import com.example.legostore.data.dao.ProductDao
import com.example.legostore.data.entity.Product

class ProductRepository(private val productDao: ProductDao) {
    fun getAllProducts(): LiveData<List<Product>> = productDao.getAllProducts()

    suspend fun getProductById(id: Long): Product? = productDao.getProductById(id)

    suspend fun insertProduct(product: Product): Long = productDao.insertProduct(product)

    suspend fun insertProducts(products: List<Product>) = productDao.insertProducts(products)

    suspend fun updateProduct(product: Product) = productDao.updateProduct(product)

    suspend fun deleteProduct(product: Product) = productDao.deleteProduct(product)
} 