package com.example.currencyconverter.data.database

import android.content.Context
import androidx.room.Room
import com.example.currencyconverter.utils.Constants

class RoomDbService(private val context: Context) {
    private var db: CurrenciesDatabase? = null

    private fun getDb(): CurrenciesDatabase {
        if (db == null) {
            db = Room.databaseBuilder(context, CurrenciesDatabase::class.java, Constants.DB_NAME)
                .build()
        }
        return db!!
    }

    val currenciesDao = getDb().currenciesDao()
}