package com.alxdev.two.moneychanger.util.extension

import com.alxdev.two.moneychanger.datax.datasource.entity.Currency
import com.alxdev.two.moneychanger.datax.datasource.entity.History

fun List<History>.toModel(): List<com.alxdev.two.moneychanger.domainx.model.History> = map {
    com.alxdev.two.moneychanger.domainx.model.History(it.totalCurrencyChange())
}

fun List<Currency>.toModelCurrency(): List<com.alxdev.two.moneychanger.domainx.model.Currency> =
    this.map {
        com.alxdev.two.moneychanger.domainx.model.Currency(
            it.countryName,
            it.name,
            it.shortName,
            it.value,
            it.icon,
        )
    }