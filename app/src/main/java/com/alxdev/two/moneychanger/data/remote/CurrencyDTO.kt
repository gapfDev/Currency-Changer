package com.alxdev.two.moneychanger.data.remote

import com.alxdev.two.moneychanger.data.local.entity.Currency

data class CurrencyDTO(
    val privacy: String,
    val quotes: MutableMap<String, Double>,
    val source: String,
    val success: Boolean,
    val terms: String,
    val timestamp: Int
) {

    fun getQuotesList(): List<Currency> =
        mutableListOf<Currency>().apply {
            quotes.takeIf {
                it.isNotEmpty()
            }?.let { _quotes ->
                _quotes.forEach {
                    this.add(
                        Currency(
                            description = it.key,
                            value = it.value
                        )
                    )
                }
            } ?: emptyList<CurrencyDTO>()
        }
}