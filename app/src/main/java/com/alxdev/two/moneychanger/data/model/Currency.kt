package com.alxdev.two.moneychanger.data.model

data class Currency(
    val countryName: String,
    val name: String,
    val shortName: String,
    val value: Double,
    val iconUrl: String,
) {
    override fun toString(): String {
        return this.countryName
    }
}