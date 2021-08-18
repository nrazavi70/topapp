package com.test.topapp.framework.remote.util

import com.google.gson.Gson
import com.test.topapp.framework.remote.response.ErrorResponse
import retrofit2.HttpException

fun handleError(e: Throwable): Exception {
    if (e is HttpException) {
        e.response()?.let { res ->
            res.errorBody()?.let {
                return try {
                    val errorResponse: ErrorResponse =
                        Gson().fromJson(it.charStream(), ErrorResponse::class.java)
                    Exception(errorResponse.message)
                } catch (e: Exception) {
                    Exception("Error on server")
                }
            }
        }
        return Exception("Some thing were wrong")
    }
    return Exception("Connect to server problem")
}