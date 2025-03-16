package com.example.legostore.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.legostore.data.entity.Order
import com.example.legostore.data.entity.OrderItem
import com.example.legostore.data.entity.Product

data class OrderWithItems(
    @Embedded val order: Order,
    @Relation(
        entity = OrderItem::class,
        parentColumn = "id",
        entityColumn = "orderId"
    )
    val orderItems: List<OrderItemWithProduct>
)

data class OrderItemWithProduct(
    @Embedded val orderItem: OrderItem,
    @Relation(
        parentColumn = "productId",
        entityColumn = "id"
    )
    val product: Product
) 