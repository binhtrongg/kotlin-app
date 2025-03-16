package com.example.legostore.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.legostore.data.entity.Order
import com.example.legostore.data.entity.OrderItem
import com.example.legostore.data.model.OrderWithItems

@Dao
interface OrderDao {
    @Transaction
    @Query("SELECT * FROM orders WHERE userEmail = :userEmail ORDER BY orderDate DESC")
    fun getOrdersWithItems(userEmail: String): LiveData<List<OrderWithItems>>

    @Insert
    suspend fun insertOrder(order: Order): Long

    @Insert
    suspend fun insertOrderItems(orderItems: List<OrderItem>)

    @Transaction
    suspend fun insertOrderWithItems(order: Order, orderItems: List<OrderItem>) {
        val orderId = insertOrder(order)
        insertOrderItems(orderItems.map { it.copy(orderId = orderId) })
    }
} 