package com.example.currencyconverter.data.usecases

import android.content.Context
import com.example.currencyconverter.data.repositories.CurrenciesRepository
import com.example.currencyconverter.model.Currency
import com.example.currencyconverter.model.CurrencyEntity
import com.example.currencyconverter.utils.DataState
import retrofit2.Response

class GetCurrenciesFromDatabaseUseCase(context: Context) {
    private val currenciesRepository = CurrenciesRepository(context)
    suspend operator fun invoke(): DataState<List<Currency>> {
        val cachedCurrencies = currenciesRepository.getCurrenciesFromDb().map { it.fromEntity() }
        return if (cachedCurrencies.isNotEmpty()) {
            DataState.Success(successData = cachedCurrencies)
        } else {
            DataState.Error(message = "No internet connection")
        }
    }
}