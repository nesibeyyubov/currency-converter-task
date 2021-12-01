package com.example.currencyconverter.utils

sealed class DataState<T>(data: T? = null, errorMessage: String? = null) {
    data class Success<T>(val data: T) : DataState<T>(data = data)
    data class Error<T>(val message: String) : DataState<T>(errorMessage = message)
    class Loading<T> : DataState<T>()
}