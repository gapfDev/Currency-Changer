package com.alxdev.two.moneychanger.datasourcex.currencycountry


import com.google.gson.annotations.SerializedName

data class RegionalBloc(
    @SerializedName("acronym")
    val acronym: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("otherAcronyms")
    val otherAcronyms: List<Any?>?,
    @SerializedName("otherNames")
    val otherNames: List<String?>?
)