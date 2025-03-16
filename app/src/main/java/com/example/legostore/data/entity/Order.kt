package com.example.legostore.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "orders",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["email"],
            childColumns = ["userEmail"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userEmail: String,
    val address: String,
    val phoneNumber: String,
    val totalAmount: Double,
    val orderDate: Long = System.currentTimeMillis()
) 