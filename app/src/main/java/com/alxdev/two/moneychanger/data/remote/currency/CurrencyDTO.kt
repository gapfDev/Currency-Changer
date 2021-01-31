package com.alxdev.two.moneychanger.data.remote.currency

import com.alxdev.two.moneychanger.data.local.entity.Currency
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

data class CurrencyDTO(
    val privacy: String,
    val quotes: MutableMap<String, Double>,
    val source: String,
    val success: Boolean,
    val terms: String,
    val timestamp: Int
) {

    fun cleanCurrencyNameMap(): Map<String, Double> {
        return quotes.mapKeys {
            it.key.replace("USD", "")
        }
    }


}

suspend fun CurrencyDTO.toCurrencyEntity(
    action: suspend (String, Double) -> Currency?
): List<Currency> {
    val currencyCountryList = mutableListOf<Currency>()

    val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("Caught $exception")
    }

    supervisorScope {
        cleanCurrencyNameMap().forEach { _currencyMap ->
            launch(Dispatchers.IO + exceptionHandler) {
                action(_currencyMap.key, _currencyMap.value).let { _currencyEntity ->
                        _currencyEntity?.let { currencyCountryList.add(it) }
                    }
            }
        }
    }
    return currencyCountryList
}