package com.example.peoplesontrol.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.peoplesontrol.data.api.ApiHelper
import com.example.peoplesontrol.data.db.DatabaseHelper
import com.example.peoplesontrol.data.model.*
import com.example.peoplesontrol.data.repository.ProfileRepository
import com.example.peoplesontrol.utils.Resource
import kotlinx.coroutines.Dispatchers

class ProfileViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    fun logout() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.logout()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error"))
        }
    }

    fun refreshToken() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = repository.refreshToken(
                        Message(Data.token.refreshToken.toString()),
                        "Bearer " + Data.token.refreshToken.toString()
                    )
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error"))
        }
    }

    fun getProfile() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            repository.clearProfile()
            repository.insertProfileToDB(repository.getProfile())
            emit(Resource.success(data = repository.getProfile()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error"))
        }
    }

    fun getProfileFromDB() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.getProfileFromDB()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error"))
        }
    }

    fun updateProfile(id: Long, profile: ProfilePost) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.updateProfile(id, profile)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error"))
        }
    }

    fun deleteProfile(id: Long) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.deleteProfile(id)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error"))
        }
    }

    fun createRequest(request: RequestPost) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.createRequest(request)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error"))
        }
    }

    fun updateRequest(id: Long, request: RequestPost) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.updateRequest(id, request)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error"))
        }
    }

    fun deleteRequest(id: Long) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.deleteRequest(id)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error"))
        }
    }
}

class ProfileViewModelFactory(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(ProfileRepository(apiHelper, dbHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}