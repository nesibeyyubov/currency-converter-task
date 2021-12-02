package com.example.currencyconverter.data.usecases

import com.example.currencyconverter.model.Currency

class CalculateExchangeResultUseCase {

    suspend operator fun invoke(
        currentAmount: Double,
        currencyList: List<Currency>,
        selectedToCurrency: String,
        selectedFromCurrency: String
    ): String {
        currencyList.let {
            val currency = it.find { cur -> cur.code == selectedToCurrency }
            if (currency != null) {
                if (selectedToCurrency == selectedFromCurrency) {
                    return currentAmount.toString()
                } else {
                    return (currency.rate * currentAmount).toString()
                }
            }
        }
        return "0.0"
    }

}