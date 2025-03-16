package com.example.legostore.repository

import com.example.legostore.data.dao.UserDao
import com.example.legostore.data.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: UserDao) {
    
    suspend fun registerUser(email: String, password: String, fullName: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val existingUser = userDao.getUserByEmail(email)
                if (existingUser != null) {
                    Result.failure(Exception("User with this email already exists"))
                } else {
                    userDao.insertUser(User(email, password, fullName))
                    Result.success(Unit)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun loginUser(email: String, password: String): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val user = userDao.login(email, password)
                if (user != null) {
                    Result.success(user)
                } else {
                    Result.failure(Exception("Invalid email or password"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
} 