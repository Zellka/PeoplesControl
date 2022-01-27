package com.example.peoplesontrol.data.api

import com.example.peoplesontrol.data.model.*

class ApiHelper(private val apiService: ApiService) {

    suspend fun getCategories() = apiService.getCategories()

    suspend fun getRequestsByCategoryId(id: Int) = apiService.getRequestsByCategoryId(id)

    suspend fun getRequests() = apiService.getRequests()

    suspend fun watchRequest(id: Long) = apiService.watchRequest(id)

    suspend fun createRequest(request: RequestPost) = apiService.createRequest(request)

    suspend fun updateRequest(id: Long, request: RequestPost) =
        apiService.updateRequest(id, request)

    suspend fun deleteRequest(id: Long) = apiService.deleteRequest(id)

    suspend fun login(login: Login) = apiService.login(login)

    suspend fun logout() = apiService.logout()

    suspend fun signup(signup: Signup) = apiService.signup(signup)

    suspend fun refreshToken(message: Message, token: String) =
        apiService.refreshToken(message, token)

    suspend fun getProfile() = apiService.getProfile()

    suspend fun updateProfile(id: Long, profile: ProfilePost) =
        apiService.updateProfile(id, profile)

    suspend fun deleteProfile(id: Long) = apiService.deleteProfile(id)
}