package com.example.currencyconverter.utils

sealed class DataState<T>(val data: T? = null, errorMessage: String? = null) {
    data class Success<T>(val successData: T) : DataState<T>(data = successData)
    data class Error<T>(val message: String) : DataState<T>(errorMessage = message)
    class Loading<T> : DataState<T>()
}