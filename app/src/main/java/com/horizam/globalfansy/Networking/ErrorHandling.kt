package com.horizam.globalfansy.Networking

import com.horizam.globalfansy.Utils.Constants
import retrofit2.Response

interface BaseErrorHandler {
    val statusCode: Int
    fun handleError(): Throwable
}

data class ErrorResponse(val success : Boolean = true, val message: String)

class ClientSideError(val statusCode: Int, override val message: String) : Throwable(message)
class ServerSideError(val statusCode: Int, override val message: String) : Throwable(message)
class UnknownError(override val message: String) : Throwable(message)

class DefaultErrorHandler<T>(private val response: Response<T>) : BaseErrorHandler {
    override val statusCode: Int
        get() = response.code()

    override fun handleError(): Throwable {
        return when (response.code()) {
            in 400..499 -> {
                ClientSideError(statusCode, response.message())
            }
            in 500..599 -> {
                ServerSideError(statusCode, response.message())

            }
            else -> {
                UnknownError(Constants.STR_UNKNOWN_ERROR)
            }
        }
    }

}