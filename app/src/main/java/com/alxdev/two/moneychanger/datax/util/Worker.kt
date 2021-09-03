package com.alxdev.two.moneychanger.datax.util

import com.alxdev.two.moneychanger.datax.datasource.entity.Currency
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

object Worker {

    suspend fun getCurrencyList(
        countryMap: Map<String, Double>,
        callDataSource: suspend (String, Double) -> Currency?
    ): List<Currency> {
        val currencyCountryList = mutableListOf<Currency>()

        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            println("Caught $exception")
        }

        supervisorScope {
            for ((key, value) in countryMap) {
                launch(Dispatchers.IO + exceptionHandler) {
                    callDataSource(key, value).let { _currencyEntity ->
                        _currencyEntity?.let { currencyCountryList.add(it) }
                    }
                }
            }
        }
        return currencyCountryList
    }
}