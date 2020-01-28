package com.alxdev.two.moneychanger.ui.changer

import com.alxdev.two.moneychanger.data.local.entity.Currency
import com.alxdev.two.moneychanger.data.local.entity.History
import com.alxdev.two.moneychanger.data.remote.currencycountry.CurrencyCountryDTO
import com.alxdev.two.moneychanger.data.toCurrencyFormat
import kotlinx.coroutines.*


fun CurrencyInformationDTO.toHistory() = History(
    localCountry = localCountry,
    foreignCountry = foreignCountry,
    localCurrencyQuantity = localCurrencyQuantity,
    foreignCurrencyQuantity = foreignCurrencyQuantity
)

fun History.totalCurrencyChange(): String {
    val localQuantity = localCurrencyQuantity.toCurrencyFormat()
    val foreignQuantity = foreignCurrencyQuantity.toCurrencyFormat()
    val total =
        (localCurrencyQuantity * foreignCurrencyQuantity).toCurrencyFormat()
    return "Local country : $localCountry \r\nCurrency Quantity = $localQuantity \r\nForeign Country : $foreignCountry \r\nCurrency Value = $foreignQuantity \r\nConversion : $total"
}

fun CurrencyCountryDTO.toCurrency(quote: Map.Entry<String, Double>): Currency? {
    val currencyName = currencies?.get(0)?.name ?: ""
    val currencyValue = quote.value
    val currencyShortName = alpha3Code ?: ""
    val currencyCountryName = name ?: ""
    val currencyIconFlag = flag ?: ""

    return when {
        currencyName.isNotBlank() -> {
            Currency(
                name = currencyName,
                value = currencyValue,
                shortName = currencyShortName,
                countryName = currencyCountryName,
                icon = currencyIconFlag
            )
        }
        else -> null
    }
}

class CurrencyInformationDTO(
    val localCountry: String,
    val foreignCountry: String,
    val localCurrencyQuantity: Double,
    val foreignCurrencyQuantity: Double
)

class HistoryItem(val itemText: String)

object Coroutines {
    fun io(work: suspend (() -> Unit)): Job =
        CoroutineScope(Dispatchers.IO).launch {
            work()
        }

    fun <T : Any> ioThenMain(work: suspend (() -> T?), callback: ((T?) -> Unit)): Job =
        CoroutineScope(Dispatchers.Main).launch {
            val data = CoroutineScope(Dispatchers.IO).async rt@{
                return@rt work()
            }.await()
            callback(data)
        }
}