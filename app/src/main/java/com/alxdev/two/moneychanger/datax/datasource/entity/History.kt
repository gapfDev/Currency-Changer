package com.alxdev.two.moneychanger.datax.datasource.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = History.TABLE_NAME)
class History(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "currency_local_country")
    val localCountry: String,

    @ColumnInfo(name = "currency_foreign_country")
    val foreignCountry: String,

    @ColumnInfo(name = "local_currency_quantity")
    val localCurrencyQuantity: Double,

    @ColumnInfo(name = "foreign_currency_quantity")
    val foreignCurrencyQuantity: Double

//    @ColumnInfo(name = "time_millis")
//    val timeMillis : String
) {

    companion object {
        const val TABLE_NAME = "change_history"
    }
}
