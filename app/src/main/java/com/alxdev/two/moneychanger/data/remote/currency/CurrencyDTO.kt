package com.alxdev.two.moneychanger.data.remote.currency

import com.alxdev.two.moneychanger.data.local.entity.Currency

data class CurrencyDTO(
    val privacy: String,
    val quotes: MutableMap<String, Double>,
    val source: String,
    val success: Boolean,
    val terms: String,
    val timestamp: Int
)