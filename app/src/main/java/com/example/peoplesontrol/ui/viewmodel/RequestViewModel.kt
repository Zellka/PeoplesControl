package com.example.peoplesontrol.ui.viewmodel

import androidx.lifecycle.*
import com.example.peoplesontrol.data.api.ApiHelper
import com.example.peoplesontrol.data.db.DatabaseHelper
import com.example.peoplesontrol.data.model.Category
import com.example.peoplesontrol.data.model.Data
import com.example.peoplesontrol.data.model.Message
import com.example.peoplesontrol.data.model.RequestPost
import com.example.peoplesontrol.data.repository.RequestRepository
import com.example.peoplesontrol.utils.Resource
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RequestViewModel(
    private val repository: RequestRepository
) : ViewModel() {

    fun getCategories() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            repository.clearCategories()
            repository.insertCategoriesToDB(repository.getCategories().data)
            emit(Resource.success(data = repository.getCategories().data))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error"))
        }
    }

    fun getCategoriesFromDB() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.getCategoriesFromDB()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error"))
        }
    }

    fun getRequestsByCategoryId(id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.getRequestsByCategoryId(id).data))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error"))
        }
    }

    fun getRequests() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            repository.clearRequests()
            repository.insertRequestsToDB(repository.getRequests().data)
            emit(Resource.success(data = repository.getRequests().data))

        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error"))
        }
    }

    fun getRequestsFromDB() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.getRequestsFromDB()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error"))
        }
    }

    fun createRequest(request: RequestPost, files: HashMap<String, Array<RequestBody>>) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = repository.createRequest(request, files)))
            } catch (exception: Exception) {
                emit(Resource.error(data = null, message = exception.message ?: "Error"))
            }
        }


    fun watchRequest(id: Long) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.watchRequest(id)))
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
}

class ViewModelFactory(private val apiHelper: ApiHelper, private val dbHelper: DatabaseHelper) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RequestViewModel::class.java)) {
            return RequestViewModel(RequestRepository(apiHelper, dbHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}