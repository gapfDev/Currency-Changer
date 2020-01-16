package com.alxdev.two.moneychanger.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.alxdev.two.moneychanger.data.toCurrencyFormat

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

    @Ignore
    fun totalCurrencyChange(): String {
        val localQuantity = localCurrencyQuantity.toCurrencyFormat()
        val foreignQuantity = foreignCurrencyQuantity.toCurrencyFormat()
        val total =
            (localCurrencyQuantity * foreignCurrencyQuantity).toCurrencyFormat()
        return "Local country : ${localCountry} \r\nCurrency Quantity = $localQuantity \r\nForeign Country : ${foreignCountry} \r\nCurrency Quantity = $foreignQuantity \r\nConversion : $total"
    }
}
