package com.example.currencyconverter.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.currencyconverter.utils.Constants

@Entity(tableName = Constants.DB_TABLE_NAME)
class CurrencyEntity(
    val code: String,
    val alphaCode: String,
    @PrimaryKey val numericCode: String,
    val name: String,
    val rate: Double,
    val date: String,
    val inverseRate: Double
){
    fun fromEntity() = Currency(
        code = code,
        alphaCode = alphaCode,
        numericCode = numericCode,
        name = name,
        rate = rate,
        date = date,
        inverseRate = inverseRate
    )
}