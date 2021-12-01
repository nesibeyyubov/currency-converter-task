package com.example.currencyconverter.model

class Currency(
    val code: String,
    val alphaCode: String,
    val numericCode: String,
    val name: String,
    val rate: Double,
    val date: String,
    val inverseRate: Double
){
    fun toEntity() = CurrencyEntity(
        code = code,
        alphaCode = alphaCode,
        numericCode = numericCode,
        name = name,
        rate = rate,
        date = date,
        inverseRate = inverseRate
    )
}