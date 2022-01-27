package com.example.peoplesontrol.data.model

import com.google.android.gms.maps.model.LatLng

data class Login(val username: String, val password: String)

data class Signup(val name: String, val username: String, val password: String)

data class TokenData(val accessToken: String?, val refreshToken: String?, val tokenType: String?)

object Data {
    var token: TokenData = TokenData("", "", "")
    var categoryId: Int = 1
    var isNetworkConnected = true
    var point: LatLng? = null
}

data class Message(val mes: String)
