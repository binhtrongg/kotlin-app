package com.example.legostore.repository

import androidx.lifecycle.LiveData
import com.example.legostore.data.dao.CartDao
import com.example.legostore.data.entity.CartItem
import com.example.legostore.data.model.CartItemWithProduct

class CartRepository(private val cartDao: CartDao) {
    fun getCartItems(userEmail: String): LiveData<List<CartItemWithProduct>> =
        cartDao.getCartItemsWithProducts(userEmail)

    suspend fun addToCart(userEmail: String, productId: Long, quantity: Int = 1) {
        val existingItem = cartDao.getCartItem(userEmail, productId)
        if (existingItem != null) {
            cartDao.updateCartItem(existingItem.copy(quantity = existingItem.quantity + quantity))
        } else {
            cartDao.insertCartItem(CartItem(productId = productId, userEmail = userEmail, quantity = quantity))
        }
    }

    suspend fun updateQuantity(cartItem: CartItem) = cartDao.updateCartItem(cartItem)

    suspend fun removeFromCart(cartItem: CartItem) = cartDao.deleteCartItem(cartItem)

    suspend fun clearCart(userEmail: String) = cartDao.clearCart(userEmail)
} 