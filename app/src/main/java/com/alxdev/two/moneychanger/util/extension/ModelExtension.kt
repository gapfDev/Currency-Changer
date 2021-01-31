package com.alxdev.two.moneychanger.util.extension

import com.alxdev.two.moneychanger.data.local.entity.Currency

fun List<Currency>.toModelCurrency(): List<com.alxdev.two.moneychanger.data.model.Currency> =
    this.map {
        com.alxdev.two.moneychanger.data.model.Currency(
            it.countryName,
            it.name,
            it.shortName,
            it.value,
            it.icon,
        )
    }