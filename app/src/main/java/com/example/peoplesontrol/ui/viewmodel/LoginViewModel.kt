package com.example.peoplesontrol.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.peoplesontrol.data.api.ApiHelper
import com.example.peoplesontrol.data.db.DatabaseHelper
import com.example.peoplesontrol.data.model.Login
import com.example.peoplesontrol.data.model.Message
import com.example.peoplesontrol.data.model.Signup
import com.example.peoplesontrol.data.repository.ProfileRepository
import com.example.peoplesontrol.utils.Resource
import kotlinx.coroutines.Dispatchers

class LoginViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    fun login(login: Login) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.login(login)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error"))
        }
    }

    fun signup(signup: Signup) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.signup(signup)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error"))
        }
    }

    fun refreshToken(message: Message, token: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.refreshToken(message, token)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error"))
        }
    }
}

class LoginViewModelFactory(private val apiHelper: ApiHelper,
                            private val dbHelper: DatabaseHelper) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(ProfileRepository(apiHelper, dbHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}