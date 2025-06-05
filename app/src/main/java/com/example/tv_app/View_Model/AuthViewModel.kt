package com.example.tv_app.View_Model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tv_app.Room.AppDatabase
import com.example.tv_app.Room.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = AppDatabase.getDatabase(application).userDao()

    fun signup(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val exists = withContext(Dispatchers.IO) {
                userDao.getUserByEmail(email.trim()) != null
            }
            if (!exists) {
                withContext(Dispatchers.IO) {
                    userDao.insertUser(User(email = email.trim(), password = password.trim()))
                }
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = withContext(Dispatchers.IO) {
                userDao.login(email.trim(), password.trim())
            }
            onResult(user != null)
        }
    }

}