package com.alxdev.two.moneychanger.datax.datasource.util

import com.alxdev.two.moneychanger.datasourcex.currency.ContryDTO

fun ContryDTO.cleanCurrencyNameMap(): Map<String, Double> {
    return quotes.mapKeys {
        it.key.replace("USD", "")
    }
}