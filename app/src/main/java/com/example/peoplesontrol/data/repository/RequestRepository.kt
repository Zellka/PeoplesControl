package com.example.peoplesontrol.data.repository

import com.example.peoplesontrol.data.api.ApiHelper
import com.example.peoplesontrol.data.db.DatabaseHelper
import com.example.peoplesontrol.data.model.Category
import com.example.peoplesontrol.data.model.Message
import com.example.peoplesontrol.data.model.Request
import com.example.peoplesontrol.data.model.RequestPost
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RequestRepository(private val apiHelper: ApiHelper, private val dbHelper: DatabaseHelper) {

    suspend fun getCategories() = apiHelper.getCategories()

    suspend fun watchRequest(id: Long) = apiHelper.watchRequest(id)

    suspend fun getCategoriesFromDB() = dbHelper.getCategories()

    suspend fun clearCategories() = dbHelper.clearCategories()

    suspend fun insertCategoriesToDB(categories: List<Category>) =
        dbHelper.insertCategories(categories)

    suspend fun getRequestsByCategoryId(id: Int) = apiHelper.getRequestsByCategoryId(id)

    suspend fun refreshToken(message: Message, token: String) =
        apiHelper.refreshToken(message, token)

    suspend fun getRequests() = apiHelper.getRequests()

    suspend fun clearRequests() = dbHelper.clearRequests()

    suspend fun getRequestsFromDB() = dbHelper.getRequests()

    suspend fun insertRequestsToDB(requests: List<Request>) =
        dbHelper.insertRequests(requests)

    suspend fun createRequest(request: RequestPost, files: HashMap<String, Array<RequestBody>>) =
        apiHelper.createRequest(request, files)

}