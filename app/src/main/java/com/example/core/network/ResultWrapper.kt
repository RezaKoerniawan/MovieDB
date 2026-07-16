package com.example.core.network

import java.io.IOException
import java.net.SocketTimeoutException
import retrofit2.HttpException

sealed class ResultWrapper<out T> {
    data class Success<out T>(val data: T) : ResultWrapper<T>()
    data class Error(val errorType: ErrorType, val message: String? = null, val throwable: Throwable? = null) : ResultWrapper<Nothing>()
    object Loading : ResultWrapper<Nothing>()
}

sealed class ErrorType {
    object NoInternet : ErrorType()
    object Timeout : ErrorType()
    object Unauthorized : ErrorType()
    object NotFound : ErrorType()
    object ServerError : ErrorType()
    object EmptyResponse : ErrorType()
    object Unknown : ErrorType()
}

fun <T> Throwable.toResultWrapper(): ResultWrapper<T> {
    return when (this) {
        is SocketTimeoutException -> ResultWrapper.Error(
            errorType = ErrorType.Timeout,
            message = "Koneksi habis (Timeout). Silakan coba lagi nanti.",
            throwable = this
        )
        is IOException -> ResultWrapper.Error(
            errorType = ErrorType.NoInternet,
            message = "Tidak ada koneksi internet. Periksa koneksi Anda.",
            throwable = this
        )
        is HttpException -> {
            val code = this.code()
            val errorType = when (code) {
                401 -> ErrorType.Unauthorized
                404 -> ErrorType.NotFound
                in 500..599 -> ErrorType.ServerError
                else -> ErrorType.Unknown
            }
            val msg = when (code) {
                401 -> "Sesi tidak sah (401). API Key Anda mungkin salah atau kedaluwarsa."
                404 -> "Konten tidak ditemukan (404)."
                in 500..599 -> "Terjadi kesalahan pada server (HTTP $code). Silakan coba lagi."
                else -> "Terjadi kesalahan jaringan (HTTP $code)."
            }
            ResultWrapper.Error(
                errorType = errorType,
                message = msg,
                throwable = this
            )
        }
        else -> ResultWrapper.Error(
            errorType = ErrorType.Unknown,
            message = this.localizedMessage ?: "Terjadi kesalahan yang tidak diketahui.",
            throwable = this
        )
    }
}
