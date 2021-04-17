package com.alxdev.two.moneychanger.data.remote.currency

data class ContryDTO(
    val privacy: String,
    val quotes: MutableMap<String, Double>,
    val source: String,
    val success: Boolean,
    val terms: String,
    val timestamp: Int
)