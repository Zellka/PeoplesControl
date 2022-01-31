package com.example.peoplesontrol.data.api

import com.example.peoplesontrol.data.model.*
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @GET("problem-categories")
    suspend fun getCategories(): CategoryResponse

    @GET("problem-categories/{id}/active-requests")
    suspend fun getRequestsByCategoryId(@Path("id") id: Int): RequestResponse

    @GET("requests")
    suspend fun getRequests(): RequestResponse

    @Multipart
    @POST("requests")
    suspend fun createRequest(
        @Part("json_data") json_data: RequestPost,
        @PartMap files: HashMap<String, Array<RequestBody>>,
        @Header("Authorization") token: String = "Bearer " + Data.token.accessToken
    ): Request

    @PUT("requests/{id}")
    suspend fun updateRequest(
        @Path("id") id: Long,
        @Body request: RequestPost,
        @Header("Authorization") token: String = "Bearer " + Data.token.accessToken
    ): Request

    @DELETE("requests/{id}")
    suspend fun deleteRequest(
        @Path("id") id: Long,
        @Header("Authorization") token: String = "Bearer " + Data.token.accessToken
    ): Request

    @POST("requests/{id}/watch")
    suspend fun watchRequest(
        @Path("id") id: Long,
        @Header("Authorization") token: String = "Bearer " + Data.token.accessToken
    ): String

    @POST("auth/login")
    suspend fun login(@Body login: Login): TokenData

    @POST("auth/logout")
    suspend fun logout(@Header("Authorization") token: String = "Bearer " + Data.token.accessToken): Message

    @POST("auth/signup")
    suspend fun signup(@Body signup: Signup): Message

    @POST("auth/refreshtoken")
    suspend fun refreshToken(
        @Body refreshToken: Message,
        @Header("Authorization") token: String
    ): TokenData

    @GET("profile")
    suspend fun getProfile(@Header("Authorization") token: String = "Bearer " + Data.token.accessToken): Profile

    @PUT("profiles/{id}")
    suspend fun updateProfile(
        @Path("id") id: Long,
        @Body profile: ProfilePost,
        @Header("Authorization") token: String = "Bearer " + Data.token.accessToken
    ): Profile

    @DELETE("profiles/{id}")
    suspend fun deleteProfile(
        @Path("id") id: Long,
        @Header("Authorization") token: String = "Bearer " + Data.token.accessToken
    ): Profile
}