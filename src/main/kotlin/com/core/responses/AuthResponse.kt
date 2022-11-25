package com.core.responses

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String? = null,
    val userId: String? = null
)

data class CronosResponse<T>(val data: T? = null, val message: String)

sealed class ApiResponse<T>(val successful: Boolean, val data: T?, val message: String? = null, val code: Int? = null) {
    class Success<T>(data: T?) : ApiResponse<T>(successful = true, data) {
        companion object {
            operator fun invoke() = Success(null)
        }
    }

    class Error<T>(message: String, code: Int?, data: T?) : ApiResponse<T>(successful = false, data, message, code) {
        companion object {
            operator fun invoke(message: String, code: Int? = null) = Error(message, code, null)
        }
    }
}