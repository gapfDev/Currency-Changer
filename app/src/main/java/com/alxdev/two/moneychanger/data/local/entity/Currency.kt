package com.alxdev.two.moneychanger.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Currency.TABLE_NAME)
data class Currency(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "description")
    var description:String = "demo",

    @ColumnInfo(name = "icon")
    var icon:String = "no",

    @ColumnInfo(name = "value")
    var value:Double = 0.0

) {

    companion object {
        const val TABLE_NAME = "currency"
    }

    override fun toString(): String {
        return this.description
    }
}
