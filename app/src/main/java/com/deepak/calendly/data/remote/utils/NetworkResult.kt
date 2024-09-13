package com.deepak.calendly.data.remote.utils

import retrofit2.Response

suspend fun <T> getResult(call: suspend () -> Response<T>): Result<T> {
    return try {
        val response = call()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                Result.Success(body)
            } else {
                Result.Error("Invalid response from the server", ErrorType.InvalidData)
            }

        } else {
            val errorMessage = try {
                response.errorBody()?.string() ?: "Something went wrong"
            } catch (e: Exception) {
                "Something went wrong"
            }

            when (response.code()) {
                400 -> Result.Error(
                    errorMessage,
                    ErrorType.HttpError.BadRequest(response.code())
                )

                401 -> Result.Error(
                    errorMessage,
                    ErrorType.HttpError.Unauthorized(response.code())
                )

                403 -> Result.Error(
                    errorMessage,
                    ErrorType.HttpError.ResourceForbidden(response.code())
                )

                404 -> Result.Error(
                    errorMessage,
                    ErrorType.HttpError.ResourceNotFound(response.code())
                )

                500 -> Result.Error(
                    errorMessage,
                    ErrorType.HttpError.InternalServerError(response.code())
                )

                502 -> Result.Error(
                    errorMessage,
                    ErrorType.HttpError.BadGateWay(response.code())
                )

                301 -> Result.Error(
                    errorMessage,
                    ErrorType.HttpError.ResourceRemoved(response.code())
                )

                302 -> Result.Error(
                    errorMessage,
                    ErrorType.HttpError.RemovedResourceFound(response.code())
                )

                else -> Result.Error(
                    errorMessage,
                    ErrorType.HttpError.OtherError(response.code())
                )
            }
        }
    } catch (throwable: Throwable) {
        Result.Error(throwable.localizedMessage!!, ErrorType.NetworkException)
    }
}

sealed class Result<out T> {

    data class Success<out T>(val data: T) : Result<T>()

    data class Error(val message: String, val type: ErrorType) : Result<Nothing>()
}

sealed class ErrorType {
    data object InvalidData : ErrorType()
    data object NetworkException : ErrorType()

    sealed class HttpError(open val statusCode: Int) : ErrorType() {

        data class BadRequest(override val statusCode: Int) : HttpError(statusCode)
        data class Unauthorized(override val statusCode: Int) : HttpError(statusCode)
        data class ResourceForbidden(override val statusCode: Int) : HttpError(statusCode)
        data class ResourceNotFound(override val statusCode: Int) : HttpError(statusCode)
        data class InternalServerError(override val statusCode: Int) : HttpError(statusCode)
        data class BadGateWay(override val statusCode: Int) : HttpError(statusCode)
        data class ResourceRemoved(override val statusCode: Int) : HttpError(statusCode)
        data class RemovedResourceFound(override val statusCode: Int) : HttpError(statusCode)
        data class OtherError(override val statusCode: Int) : HttpError(statusCode)
    }
}