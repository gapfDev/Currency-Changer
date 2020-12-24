package com.alxdev.two.moneychanger.data.remote.currency

import kotlinx.coroutines.launch

data class CurrencyDTO(
    val privacy: String,
    val quotes: MutableMap<String, Double>,
    val source: String,
    val success: Boolean,
    val terms: String,
    val timestamp: Int
){

    fun getCurrencyNameMap(): Map<String, Double> {
        return quotes.mapKeys {
            it.key.replace("USD", "")
        }
    }
}