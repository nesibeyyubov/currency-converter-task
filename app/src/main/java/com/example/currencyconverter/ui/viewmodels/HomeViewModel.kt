package com.example.currencyconverter.ui.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.data.CurrenciesRepository
import com.example.currencyconverter.model.Currency
import com.example.currencyconverter.utils.Constants
import com.example.currencyconverter.utils.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val currenciesRepository = CurrenciesRepository(application)

    private val _currencies = MutableLiveData<DataState<List<Currency>>>(DataState.Loading())
    val currencies: LiveData<DataState<List<Currency>>>
        get() = _currencies

    private val _selectedFromCurrency = MutableLiveData<String>("EUR")
    val selectedFromCurrency: LiveData<String>
        get() = _selectedFromCurrency

    private val _selectedToCurrency = MutableLiveData<String>("AZN")
    val selectedToCurrency: LiveData<String>
        get() = _selectedToCurrency

    private val _exchangeResult = MutableLiveData<String>()
    val exchangeResult: LiveData<String>
        get() = _exchangeResult


    fun setCurrencyType(currencyType: String, currencyCode: String) {
        if (currencyType == Constants.VALUE_CURRENCY_FROM) {
            _selectedFromCurrency.value = currencyCode
        } else {
            _selectedToCurrency.value = currencyCode
        }
    }

    fun swapCurrencies(){
        val selectedFromCurrencyValue = selectedFromCurrency.value
        _selectedFromCurrency.value = selectedToCurrency.value
        _selectedToCurrency.value = selectedFromCurrencyValue!!
    }

    fun calculateExchangeResult(currentAmount: Double) =
        viewModelScope.launch(Dispatchers.Default) {
            _currencies.value?.data?.let {
                val currency = it.find { cur -> cur.code == selectedToCurrency.value }
                if (currency != null) {
                    _exchangeResult.postValue((currency.rate * currentAmount).toString())
                }
            }

        }


    fun getCurrencies() = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (hasInternetConnection()) {
                val response =
                    currenciesRepository.getCurrencies(baseCurrency = selectedFromCurrency.value)
                val handledResponse = handleResponse(response)
                if (handledResponse is DataState.Success) {
                    handledResponse.data!!.map { it.toEntity() }
                        .let { currenciesRepository.insertCurrencies(it) }
                    val currencies =
                        currenciesRepository.getCurrenciesFromDb().map { it.fromEntity() }
                    _currencies.postValue(DataState.Success(currencies))
                } else {
                    _currencies.postValue(handledResponse)
                }
            } else {
                val cachedCurrencies =
                    currenciesRepository.getCurrenciesFromDb().map { it.fromEntity() }
                if (cachedCurrencies.isNotEmpty()) {
                    _currencies.postValue(DataState.Success(successData = cachedCurrencies))
                } else {
                    _currencies.postValue(DataState.Error(message = "No internet connection"))
                }
            }


        } catch (e: Exception) {
            _currencies.postValue(
                DataState.Error(
                    message = e.message ?: "Something went wrong"
                )
            )
        }
    }

    private fun handleResponse(response: Response<List<Currency>>): DataState<List<Currency>> {
        when (response.code()) {
            200, 201 -> {
                return DataState.Success(successData = response.body()!!)
            }
            500, 404 -> {
                return DataState.Error(message = response.message())
            }

        }
        return DataState.Loading()
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}