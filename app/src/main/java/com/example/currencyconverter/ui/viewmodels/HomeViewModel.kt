package com.example.currencyconverter.ui.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.data.repositories.CurrenciesRepository
import com.example.currencyconverter.data.usecases.CalculateExchangeResultUseCase
import com.example.currencyconverter.data.usecases.GetCurrenciesFromDatabaseUseCase
import com.example.currencyconverter.data.usecases.GetCurrenciesUseCase
import com.example.currencyconverter.data.usecases.InsertCurrenciesToDatabaseUseCase
import com.example.currencyconverter.model.Currency
import com.example.currencyconverter.utils.Constants
import com.example.currencyconverter.utils.Constants.REQUEST_CODE_GET_SUCCESS
import com.example.currencyconverter.utils.Constants.REQUEST_CODE_NOT_FOUND_ERROR
import com.example.currencyconverter.utils.Constants.REQUEST_CODE_POST_SUCCESS
import com.example.currencyconverter.utils.Constants.REQUEST_CODE_SERVER_ERROR
import com.example.currencyconverter.utils.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val getCurrenciesUseCase = GetCurrenciesUseCase(application)
    private val calculateExchangeResultUseCase = CalculateExchangeResultUseCase()
    private val getCurrenciesFromDatabaseUseCase = GetCurrenciesFromDatabaseUseCase(application)
    private val insertCurrenciesToDatabaseUseCase = InsertCurrenciesToDatabaseUseCase(application)

    private val _currencies = MutableLiveData<DataState<List<Currency>>>(DataState.Loading())
    val currencies: LiveData<DataState<List<Currency>>>
        get() = _currencies

    private val _selectedFromCurrency =
        MutableLiveData<String>(Constants.DEFAULT_SELECTED_FROM_CURRENCY)
    val selectedFromCurrency: LiveData<String>
        get() = _selectedFromCurrency

    private val _selectedToCurrency =
        MutableLiveData<String>(Constants.DEFAULT_SELECTED_TO_CURRENCY)
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

    fun swapCurrencies() {
        val selectedFromCurrencyValue = selectedFromCurrency.value
        _selectedFromCurrency.value = selectedToCurrency.value
        _selectedToCurrency.value = selectedFromCurrencyValue!!
    }

    fun calculateExchangeResult(currentAmount: Double) =
        viewModelScope.launch(Dispatchers.Default) {
            _currencies.value?.data?.let {
                val result =
                    calculateExchangeResultUseCase(
                        currentAmount,
                        it,
                        selectedToCurrency.value!!,
                        selectedFromCurrency.value!!
                    )
                _exchangeResult.postValue(result)
            }
        }


    fun getCurrencies() = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (hasInternetConnection()) {
                val response = getCurrenciesUseCase(selectedFromCurrency.value!!)
                val handledResponse = handleResponse(response)
                if (handledResponse is DataState.Success) {
                    insertCurrenciesToDatabaseUseCase(handledResponse.data!!)
                    val cachedCurrencies = getCurrenciesFromDatabaseUseCase()
                    _currencies.postValue(cachedCurrencies)
                } else {
                    _currencies.postValue(handledResponse)
                }
            } else {
                val cachedCurrencies = getCurrenciesFromDatabaseUseCase()
                _currencies.postValue(cachedCurrencies)
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
            REQUEST_CODE_GET_SUCCESS, REQUEST_CODE_POST_SUCCESS -> {
                return DataState.Success(successData = response.body()!!)
            }
            REQUEST_CODE_SERVER_ERROR, REQUEST_CODE_NOT_FOUND_ERROR -> {
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