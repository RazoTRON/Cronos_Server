package com.core.responses

sealed class Resource<T>(val successful: Boolean, val data: T?, val message: String? = null, val code: Int? = null) {
    class Success<T>(data: T?) : Resource<T>(successful = true, data) {
        companion object {
            operator fun invoke() = Success(null)
        }
    }

    class Error<T>(message: String, code: Int?, data: T?) : Resource<T>(successful = false, data, message, code) {
        companion object {
            operator fun invoke(message: String, code: Int? = null) = Error(message, code, null)
        }
    }
}