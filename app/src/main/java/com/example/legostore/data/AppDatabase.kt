package com.example.legostore.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.legostore.data.dao.*
import com.example.legostore.data.entity.*

@Database(
    entities = [
        User::class,
        Product::class,
        CartItem::class,
        Order::class,
        OrderItem::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "lego_store_db"
                )
                .createFromAsset("database/initial_data.db")
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
} 