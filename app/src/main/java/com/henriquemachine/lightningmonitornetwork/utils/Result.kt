package com.henriquemachine.lightningmonitornetwork.utils

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
}
