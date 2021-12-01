package com.example.currencyconverter.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.currencyconverter.model.CurrencyEntity

@Dao
interface CurrenciesDao {

    @Query("SELECT * FROM currencies_table")
    suspend fun getCurrencies(): List<CurrencyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencies(currencies: List<CurrencyEntity>)
}