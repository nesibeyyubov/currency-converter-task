package com.example.currencyconverter.utils

object Constants {
    const val BASE_URL = "https://tayqatech.com/rates.php/"
    const val DB_TABLE_NAME = "currencies_table"
    const val DB_NAME = "currencies_db"
    const val BASE_CURRENCY = "USD"

    const val KEY_CURRENCY_TYPE = "selectedCurrencyType"
    const val VALUE_CURRENCY_FROM = "FROM"
    const val VALUE_CURRENCY_TO = "TO"

    const val REQUEST_CODE_GET_SUCCESS = 200
    const val REQUEST_CODE_POST_SUCCESS = 201
    const val REQUEST_CODE_SERVER_ERROR = 500
    const val REQUEST_CODE_NOT_FOUND_ERROR = 404

    const val DEFAULT_SELECTED_FROM_CURRENCY = "EUR"
    const val DEFAULT_SELECTED_TO_CURRENCY = "AZN"
}