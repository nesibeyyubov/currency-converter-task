package com.example.currencyconverter.ui.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.data.CurrenciesRepository
import com.example.currencyconverter.model.Currency
import com.example.currencyconverter.model.CurrencyEntity
import com.example.currencyconverter.utils.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class CurrencyViewModel(application: Application) : AndroidViewModel(application) {
    private val currenciesRepository = CurrenciesRepository(application)

    private val _currencies = MutableLiveData<DataState<List<Currency>>>()
    val currencies: LiveData<DataState<List<Currency>>>
        get() = _currencies


    fun getCurrencies() = viewModelScope.launch(Dispatchers.IO) {
        try {
            _currencies.postValue(DataState.Loading())
            val cachedCurrencies = currenciesRepository.getCurrenciesFromDb().map { it.fromEntity() }
            _currencies.postValue(DataState.Success(data = cachedCurrencies))

        }catch (e:Exception){
            _currencies.postValue(DataState.Error(message = "Something went wrong"))
        }

    }

}