package com.alxdev.two.moneychanger.datax.datasource.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Currency.TABLE_NAME)
data class Currency(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_currency_info")
    val id: Long = 0L,

    @ColumnInfo(name = "currency_name")
    var name: String = "Dollar",

    @ColumnInfo(name = "currency_value")
    var value: Double = 0.0,

    @ColumnInfo(name = "currency_short_name")
    var shortName: String = "USD",

    @ColumnInfo(name = "currency_country_name")
    var countryName: String = "United State of America",

    @ColumnInfo(name = "currency_country_icon")
    var icon: String = "testString"
) {

    companion object {
        const val TABLE_NAME = "currency_info"
    }
}
