package com.alxdev.two.moneychanger.util.extension

import com.alxdev.two.moneychanger.data.local.entity.Currency
import com.alxdev.two.moneychanger.data.local.entity.History
import com.alxdev.two.moneychanger.data.model.CurrencyInformation
import com.alxdev.two.moneychanger.data.remote.currencycountry.CurrencyCountryDTO

fun CurrencyCountryDTO.toCurrencyEntity(currencyValue: Double): Currency? {
    val currencyName = currencies?.get(0)?.name ?: ""
    val currencyValue = currencyValue
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

fun CurrencyInformation.toHistoryEntity() = History(
    localCountry = localCountry,
    foreignCountry = foreignCountry,
    localCurrencyQuantity = localCurrencyQuantity,
    foreignCurrencyQuantity = foreignCurrencyQuantity
)