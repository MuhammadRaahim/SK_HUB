package com.horizam.skbhub.Networking

import com.horizam.skbhub.Networking.Response.RegisterUserResponse
import com.horizam.skbhub.Networking.RetrofitClient.getApiEndpointImpl

abstract class NetworkingHelper {
    private val apiService = getApiEndpointImpl()

    fun registerUser(
        id: Int,
        apiListener: ApiListener<RegisterUserResponse>
    ){
        val call = apiService.registerUser(id)
        RetrofitClient.executeApi(call, apiListener)
    }

}