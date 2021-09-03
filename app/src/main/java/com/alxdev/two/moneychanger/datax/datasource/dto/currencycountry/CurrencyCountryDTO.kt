package com.alxdev.two.moneychanger.datasourcex.currencycountry


import com.google.gson.annotations.SerializedName

data class CurrencyCountryDTO(
    @SerializedName("name")
    val name: String?,
    @SerializedName("topLevelDomain")
    val topLevelDomain: List<String?>?,
    @SerializedName("alpha2Code")
    val alpha2Code: String?,
    @SerializedName("alpha3Code")
    val alpha3Code: String?,
    @SerializedName("callingCodes")
    val callingCodes: List<String?>?,
    @SerializedName("capital")
    val capital: String?,
    @SerializedName("altSpellings")
    val altSpellings: List<String?>?,
    @SerializedName("region")
    val region: String?,
    @SerializedName("subregion")
    val subregion: String?,
    @SerializedName("population")
    val population: Int?,
    @SerializedName("latlng")
    val latlng: List<Double?>?,
    @SerializedName("demonym")
    val demonym: String?,
    @SerializedName("area")
    val area: Double?,
    @SerializedName("timezones")
    val timezones: List<String?>?,
    @SerializedName("borders")
    val borders: List<String?>?,
    @SerializedName("nativeName")
    val nativeName: String?,
    @SerializedName("numericCode")
    val numericCode: String?,
    @SerializedName("currencies")
    val currencies: List<Currency?>?,
    @SerializedName("languages")
    val languages: List<Language?>?,
    @SerializedName("translations")
    val translations: Translations?,
    @SerializedName("flag")
    val flag: String?,
    @SerializedName("regionalBlocs")
    val regionalBlocs: List<RegionalBloc?>?,
    @SerializedName("cioc")
    val cioc: String?,
    @SerializedName("gini")
    val gini: Any?
)