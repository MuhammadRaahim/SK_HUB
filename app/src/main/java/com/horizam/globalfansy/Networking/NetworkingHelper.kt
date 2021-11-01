package com.horizam.globalfansy.Networking

import com.horizam.globalfansy.Networking.Response.RegisterUserResponse
import com.horizam.globalfansy.Networking.RetrofitClient.getApiEndpointImpl

abstract class NetworkingHelper {
    private val apiService = getApiEndpointImpl()

    fun registerUser(
        id: Int,
        apiListener: ApiListener<RegisterUserResponse>
    ){
        val call = apiService.registerUser(id)
        RetrofitClient.executeApi(call,apiListener)
    }

}