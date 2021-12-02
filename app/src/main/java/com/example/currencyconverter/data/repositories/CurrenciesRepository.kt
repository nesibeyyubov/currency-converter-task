package com.example.currencyconverter.data.repositories

import android.content.Context
import com.example.currencyconverter.data.database.RoomDbService
import com.example.currencyconverter.data.network.RetrofitService
import com.example.currencyconverter.model.Currency
import com.example.currencyconverter.model.CurrencyEntity

class CurrenciesRepository(private val context: Context) {
    private val api = RetrofitService.api
    private val db = RoomDbService(context).currenciesDao


    suspend fun getCurrencies(baseCurrency: String? = null) = api.getCurrencies(baseCurrency)
    suspend fun insertCurrencies(currencyList: List<CurrencyEntity>) =
        db.insertCurrencies(currencyList)

    suspend fun getCurrenciesFromDb() = db.getCurrencies()
}