package com.alxdev.two.moneychanger.util.extension

import com.alxdev.two.moneychanger.data.local.entity.History

fun List<History>.toModel(): List<com.alxdev.two.moneychanger.data.model.History> = map {
    com.alxdev.two.moneychanger.data.model.History(it.totalCurrencyChange())
}