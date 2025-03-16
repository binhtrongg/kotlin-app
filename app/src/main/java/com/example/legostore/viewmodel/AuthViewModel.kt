package com.example.legostore.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.legostore.repository.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: UserRepository) : ViewModel() {
    
    private val _loginResult = MutableLiveData<Result<Unit>>()
    val loginResult: LiveData<Result<Unit>> = _loginResult

    private val _registrationResult = MutableLiveData<Result<Unit>>()
    val registrationResult: LiveData<Result<Unit>> = _registrationResult

    fun login(email: String, password: String) {
        if (!validateEmail(email)) {
            _loginResult.value = Result.failure(Exception("Invalid email format"))
            return
        }

        if (password.length < 6) {
            _loginResult.value = Result.failure(Exception("Password must be at least 6 characters"))
            return
        }

        viewModelScope.launch {
            try {
                val result = repository.loginUser(email, password)
                _loginResult.value = result.map { }
            } catch (e: Exception) {
                _loginResult.value = Result.failure(e)
            }
        }
    }

    fun register(email: String, password: String, confirmPassword: String, fullName: String) {
        if (!validateEmail(email)) {
            _registrationResult.value = Result.failure(Exception("Invalid email format"))
            return
        }

        if (password.length < 6) {
            _registrationResult.value = Result.failure(Exception("Password must be at least 6 characters"))
            return
        }

        if (password != confirmPassword) {
            _registrationResult.value = Result.failure(Exception("Passwords do not match"))
            return
        }

        if (fullName.isBlank()) {
            _registrationResult.value = Result.failure(Exception("Full name is required"))
            return
        }

        viewModelScope.launch {
            try {
                val result = repository.registerUser(email, password, fullName)
                _registrationResult.value = result
            } catch (e: Exception) {
                _registrationResult.value = Result.failure(e)
            }
        }
    }

    private fun validateEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
} 