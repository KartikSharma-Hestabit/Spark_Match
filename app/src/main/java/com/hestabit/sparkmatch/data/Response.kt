package com.hestabit.sparkmatch.data

sealed class Response<out R> {
    data class Success<out R>(val result: R) : Response<R>()
    data class Failure(val exception: Exception = Exception("cannot able to process request")) : Response<Nothing>()
    data object Loading : Response<Nothing>()
}