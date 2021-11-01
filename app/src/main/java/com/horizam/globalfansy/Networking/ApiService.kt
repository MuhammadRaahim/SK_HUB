package com.horizam.globalfansy.Networking

import com.horizam.globalfansy.Networking.Response.RegisterUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    companion object{
        const val REGISTER_USER = "register"
    }
    @GET(REGISTER_USER)
    fun registerUser(@Query("id") id: Int): Call<RegisterUserResponse>
}