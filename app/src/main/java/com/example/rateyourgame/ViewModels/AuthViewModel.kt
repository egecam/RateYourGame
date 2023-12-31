package com.example.rateyourgame.ViewModels

import androidx.lifecycle.ViewModel
import com.example.rateyourgame.database.UserDao
import com.example.rateyourgame.dataclasses.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthViewModel(private val userDao: UserDao) : ViewModel() {

    private val viewModelScope = CoroutineScope(Dispatchers.IO)

    suspend fun login(username: String, password: String): User? {
        return withContext(Dispatchers.IO) {
            userDao.login(username, password)
        }
    }

    suspend fun signUp(user: User) {
        withContext(Dispatchers.IO) {
            userDao.signUp(user)
        }
    }
}
