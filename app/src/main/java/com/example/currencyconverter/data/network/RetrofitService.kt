package com.example.currencyconverter.data.network

import com.example.currencyconverter.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api:CurrencyApi = retrofit.create(CurrencyApi::class.java)

}