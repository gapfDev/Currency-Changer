package com.alxdev.two.moneychanger.datax.datasource.mapper

import com.alxdev.two.moneychanger.datasourcex.currencycountry.CurrencyCountryDTO
import com.alxdev.two.moneychanger.datax.datasource.entity.Currency
import com.alxdev.two.moneychanger.datax.datasource.entity.History
import com.alxdev.two.moneychanger.domainx.model.CurrencyInformation

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

fun List<CurrencyCountryDTO>?.toCurrencyEntity(_currencyValue: Double) : Currency? {
    return this?.get(0)?.toCurrencyEntity(_currencyValue)
}


fun CurrencyInformation.toHistoryEntity() = History(
    localCountry = localCountry,
    foreignCountry = foreignCountry,
    localCurrencyQuantity = localCurrencyQuantity,
    foreignCurrencyQuantity = foreignCurrencyQuantity
)