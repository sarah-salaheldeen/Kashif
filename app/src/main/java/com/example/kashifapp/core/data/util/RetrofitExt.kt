package com.example.kashifapp.core.data.util

import com.example.kashifapp.core.domain.util.DataError
import com.example.kashifapp.core.domain.util.Result
import okio.IOException
import retrofit2.Response
import java.net.SocketTimeoutException

suspend fun <T> safeCall(call: suspend () -> Response<T>): Result<T, DataError.Remote> {
    return try {
        val response = call()
        when {
            response.isSuccessful -> response.body()
                ?.let { Result.Success(it) }
                ?: Result.Error(DataError.Remote.UNKNOWN)
            response.code() == 400 -> Result.Error(DataError.Remote.BAD_REQUEST)
            response.code() == 406 -> Result.Error(DataError.Remote.BAD_REQUEST)
            response.code() == 408 -> Result.Error(DataError.Remote.REQUEST_TIMEOUT)
            response.code() == 429 -> Result.Error(DataError.Remote.TOO_MANY_REQUESTS)
            response.code() in 500 .. 599 -> Result.Error(DataError.Remote.SERVER)
            else -> Result.Error(DataError.Remote.UNKNOWN)
    }
} catch (e: SocketTimeoutException) {
        Result.Error(DataError.Remote.REQUEST_TIMEOUT)
    } catch (e: IOException) {
        Result.Error(DataError.Remote.NO_INTERNET)
    }
}