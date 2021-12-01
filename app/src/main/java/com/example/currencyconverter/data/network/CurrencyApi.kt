package com.example.currencyconverter.data.network

import com.example.currencyconverter.model.Currency
import com.example.currencyconverter.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    @GET(".")
    suspend fun getCurrencies(@Query("base") baseCurrency:String ?= Constants.BASE_CURRENCY):Response<List<Currency>>


}