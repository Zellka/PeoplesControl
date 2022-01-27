package com.example.peoplesontrol.data.repository

import com.example.peoplesontrol.data.api.ApiHelper
import com.example.peoplesontrol.data.db.DatabaseHelper
import com.example.peoplesontrol.data.model.*

class ProfileRepository(private val apiHelper: ApiHelper, private val dbHelper: DatabaseHelper) {

    suspend fun login(login: Login) = apiHelper.login(login)

    suspend fun logout() = apiHelper.logout()

    suspend fun signup(signup: Signup) = apiHelper.signup(signup)

    suspend fun refreshToken(message: Message, token: String) = apiHelper.refreshToken(message, token)

    suspend fun getProfile() = apiHelper.getProfile()

    suspend fun clearProfile() = dbHelper.clearProfile()

    suspend fun updateProfile(id: Long, profile: ProfilePost) = apiHelper.updateProfile(id, profile)

    suspend fun getProfileFromDB() = dbHelper.getProfile()

    suspend fun insertProfileToDB(profile: Profile) = dbHelper.insertProfile(profile)

    suspend fun deleteProfile(id: Long) = apiHelper.deleteProfile(id)

    suspend fun updateRequest(id: Long, request: RequestPost) =
        apiHelper.updateRequest(id, request)

    suspend fun deleteRequest(id: Long) = apiHelper.deleteRequest(id)

    suspend fun createRequest(request: RequestPost) = apiHelper.createRequest(request)
}