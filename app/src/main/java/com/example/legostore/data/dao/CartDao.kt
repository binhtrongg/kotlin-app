package com.example.legostore.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.legostore.data.entity.CartItem
import com.example.legostore.data.model.CartItemWithProduct

@Dao
interface CartDao {
    @Transaction
    @Query("""
        SELECT cart_items.*, products.* 
        FROM cart_items 
        INNER JOIN products ON cart_items.productId = products.id 
        WHERE cart_items.userEmail = :userEmail
    """)
    fun getCartItemsWithProducts(userEmail: String): LiveData<List<CartItemWithProduct>>

    @Query("SELECT * FROM cart_items WHERE userEmail = :userEmail AND productId = :productId")
    suspend fun getCartItem(userEmail: String, productId: Long): CartItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItem)

    @Update
    suspend fun updateCartItem(cartItem: CartItem)

    @Delete
    suspend fun deleteCartItem(cartItem: CartItem)

    @Query("DELETE FROM cart_items WHERE userEmail = :userEmail")
    suspend fun clearCart(userEmail: String)
} 