package com.example.legostore.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.legostore.data.entity.CartItem
import com.example.legostore.data.entity.Product

data class CartItemWithProduct(
    @Embedded val cartItem: CartItem,
    @Relation(
        parentColumn = "productId",
        entityColumn = "id"
    )
    val product: Product
) 