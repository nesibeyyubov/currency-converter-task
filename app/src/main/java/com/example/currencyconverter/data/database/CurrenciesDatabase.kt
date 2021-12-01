package com.example.currencyconverter.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.currencyconverter.model.CurrencyEntity


@Database(entities = [CurrencyEntity::class],version = 1)
abstract class CurrenciesDatabase : RoomDatabase() {
    abstract fun currenciesDao(): CurrenciesDao
}