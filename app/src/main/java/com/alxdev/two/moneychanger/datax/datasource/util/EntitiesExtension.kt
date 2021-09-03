package com.alxdev.two.moneychanger.util.extension

import com.alxdev.two.moneychanger.datax.datasource.entity.History


fun History.totalCurrencyChange(): String {
    val localQuantity = localCurrencyQuantity.toCurrencyFormat()
    val foreignQuantity = foreignCurrencyQuantity.toCurrencyFormat()
    val total =
        (localCurrencyQuantity * foreignCurrencyQuantity).toCurrencyFormat()
    return "Local country : $localCountry \r\nCurrency Quantity = $localQuantity \r\nForeign Country : $foreignCountry \r\nCurrency Value = $foreignQuantity \r\nConversion : $total"
}