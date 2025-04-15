package com.hestabit.sparkmatch.data

sealed class Response<out R> {
    data class Success<out R>(val result: R) : Response<R>()
    data object InitialValue : Response<Nothing>()
    data class Failure(val exception: Exception = Exception("Cannot process request")) : Response<Nothing>()
    data object Loading : Response<Nothing>()
}