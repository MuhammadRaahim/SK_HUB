package com.horizam.globalfansy.Networking

import com.horizam.globalfansy.Networking.Response.RegisterUserResponse

class NetworkingModel: NetworkingHelper() {

    fun exeConnectUserApi(
        id:Int,
        apiListener: ApiListener<RegisterUserResponse>
    ){
        registerUser(id,apiListener = apiListener)
    }
}