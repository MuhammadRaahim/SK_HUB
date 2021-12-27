package com.horizam.skbhub.Networking

import com.horizam.skbhub.Networking.Response.RegisterUserResponse

class NetworkingModel: NetworkingHelper() {

    fun exeConnectUserApi(
        id:Int,
        apiListener: ApiListener<RegisterUserResponse>
    ){
        registerUser(id,apiListener = apiListener)
    }
}