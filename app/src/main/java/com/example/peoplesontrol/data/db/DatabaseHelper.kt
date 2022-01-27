package com.example.peoplesontrol.data.db

import com.example.peoplesontrol.data.model.Category
import com.example.peoplesontrol.data.model.Profile
import com.example.peoplesontrol.data.model.Request

interface DatabaseHelper {

    suspend fun getCategories(): List<Category>

    suspend fun insertCategories(categories: List<Category>)

    suspend fun clearCategories()

    suspend fun getRequests(): List<Request>

    suspend fun insertRequests(requests: List<Request>)

    suspend fun clearRequests()

    suspend fun getProfile(): Profile

    suspend fun insertProfile(profile: Profile)

    suspend fun clearProfile()
}

class DatabaseHelperImpl(private val appDatabase: AppDatabase) : DatabaseHelper {

    override suspend fun getCategories(): List<Category> = appDatabase.categoryDao().getCategories()

    override suspend fun insertCategories(categories: List<Category>) =
        appDatabase.categoryDao().insertCategories(categories)

    override suspend fun clearCategories() = appDatabase.categoryDao().clearCategories()

    override suspend fun getRequests(): List<Request> = appDatabase.requestDao().getRequests()

    override suspend fun insertRequests(requests: List<Request>) =
        appDatabase.requestDao().insertRequests(requests)

    override suspend fun clearRequests() = appDatabase.requestDao().clearRequest()

    override suspend fun getProfile(): Profile = appDatabase.profileDao().getProfile()

    override suspend fun insertProfile(profile: Profile) =
        appDatabase.profileDao().insertProfile(profile)

    override suspend fun clearProfile() = appDatabase.profileDao().clearProfile()
}