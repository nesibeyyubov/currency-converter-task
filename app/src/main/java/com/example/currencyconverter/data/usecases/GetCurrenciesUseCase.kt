package com.example.currencyconverter.data.usecases

import android.content.Context
import com.example.currencyconverter.data.repositories.CurrenciesRepository
import com.example.currencyconverter.model.Currency
import retrofit2.Response

class GetCurrenciesUseCase(context: Context) {
    private val currenciesRepository = CurrenciesRepository(context)
    suspend operator fun invoke(baseCurrency: String): Response<List<Currency>> {
        return currenciesRepository.getCurrencies(baseCurrency = baseCurrency)
    }
}