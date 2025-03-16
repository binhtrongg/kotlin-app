package com.example.legostore.repository

import androidx.lifecycle.LiveData
import com.example.legostore.data.dao.OrderDao
import com.example.legostore.data.entity.Order
import com.example.legostore.data.entity.OrderItem
import com.example.legostore.data.model.CartItemWithProduct
import com.example.legostore.data.model.OrderWithItems

class OrderRepository(private val orderDao: OrderDao) {
    fun getOrders(userEmail: String): LiveData<List<OrderWithItems>> =
        orderDao.getOrdersWithItems(userEmail)

    suspend fun placeOrder(
        userEmail: String,
        cartItems: List<CartItemWithProduct>,
        address: String,
        phoneNumber: String
    ) {
        val totalAmount = cartItems.sumOf { it.product.price * it.cartItem.quantity }
        val order = Order(
            userEmail = userEmail,
            address = address,
            phoneNumber = phoneNumber,
            totalAmount = totalAmount
        )

        val orderItems = cartItems.map { cartItem ->
            OrderItem(
                orderId = 0, // Will be set by Room
                productId = cartItem.product.id,
                quantity = cartItem.cartItem.quantity,
                priceAtTime = cartItem.product.price
            )
        }

        orderDao.insertOrderWithItems(order, orderItems)
    }
} 