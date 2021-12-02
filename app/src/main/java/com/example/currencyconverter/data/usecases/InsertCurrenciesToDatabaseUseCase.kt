package com.example.currencyconverter.data.usecases

import android.content.Context
import com.example.currencyconverter.data.repositories.CurrenciesRepository
import com.example.currencyconverter.model.Currency
import com.example.currencyconverter.model.CurrencyEntity
import com.example.currencyconverter.utils.DataState
import retrofit2.Response

class InsertCurrenciesToDatabaseUseCase(context: Context) {
    private val currenciesRepository = CurrenciesRepository(context)
    suspend operator fun invoke(currencyList: List<Currency>) {
        currencyList.map { it.toEntity() }
            .let { currenciesRepository.insertCurrencies(it) }
    }
}