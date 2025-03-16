package com.example.legostore.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.legostore.data.model.CartItemWithProduct
import com.example.legostore.data.model.OrderWithItems
import com.example.legostore.repository.CartRepository
import com.example.legostore.repository.OrderRepository
import kotlinx.coroutines.launch

class OrderViewModel(
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository
) : ViewModel() {
    private var userEmail: String? = null
    private var _orders: LiveData<List<OrderWithItems>>? = null
    val orders: LiveData<List<OrderWithItems>>?
        get() = _orders

    private val _orderPlaced = MutableLiveData<Result<Unit>>()
    val orderPlaced: LiveData<Result<Unit>> = _orderPlaced

    fun setUser(email: String) {
        if (userEmail != email) {
            userEmail = email
            _orders = orderRepository.getOrders(email)
        }
    }

    fun placeOrder(cartItems: List<CartItemWithProduct>, address: String, phoneNumber: String) {
        userEmail?.let { email ->
            viewModelScope.launch {
                try {
                    orderRepository.placeOrder(email, cartItems, address, phoneNumber)
                    cartRepository.clearCart(email)
                    _orderPlaced.value = Result.success(Unit)
                } catch (e: Exception) {
                    _orderPlaced.value = Result.failure(e)
                }
            }
        }
    }
} 