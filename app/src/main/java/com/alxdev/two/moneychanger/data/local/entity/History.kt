package com.alxdev.two.moneychanger.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = History.TABLE_NAME)
class History(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

//    val date: Date = Date(),

    @ColumnInfo(name = "local_country")
    val localCountry: String,

    @ColumnInfo(name = "foreign_country")
    val foreignCountry: String,

    @ColumnInfo(name = "local_currency_quantity")
    val localCurrencyQuantity: Double,

    @ColumnInfo(name = "foreign_currency_quantity")
    val foreignCurrencyQuantity: Double
) {

    companion object {
        const val TABLE_NAME = "history"
    }

}
