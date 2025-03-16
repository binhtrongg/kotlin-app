package com.example.legostore.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.legostore.data.entity.CartItem
import com.example.legostore.data.model.CartItemWithProduct
import com.example.legostore.repository.CartRepository
import kotlinx.coroutines.launch

class CartViewModel(private val repository: CartRepository) : ViewModel() {
    private var userEmail: String? = null
    private var _cartItems: LiveData<List<CartItemWithProduct>>? = null
    val cartItems: LiveData<List<CartItemWithProduct>>?
        get() = _cartItems

    fun setUser(email: String) {
        if (userEmail != email) {
            userEmail = email
            _cartItems = repository.getCartItems(email)
        }
    }

    fun addToCart(productId: Long, quantity: Int = 1) {
        userEmail?.let { email ->
            viewModelScope.launch {
                repository.addToCart(email, productId, quantity)
            }
        }
    }

    fun updateQuantity(cartItem: CartItem) {
        viewModelScope.launch {
            repository.updateQuantity(cartItem)
        }
    }

    fun removeFromCart(cartItem: CartItem) {
        viewModelScope.launch {
            repository.removeFromCart(cartItem)
        }
    }

    fun clearCart() {
        userEmail?.let { email ->
            viewModelScope.launch {
                repository.clearCart(email)
            }
        }
    }

    fun calculateTotal(): Double {
        return cartItems?.value?.sumOf { it.product.price * it.cartItem.quantity } ?: 0.0
    }
} 