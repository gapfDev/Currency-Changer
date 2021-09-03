package com.alxdev.two.moneychanger.datax.datasource.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alxdev.two.moneychanger.datax.datasource.database.dao.CurrencyDao
import com.alxdev.two.moneychanger.datax.datasource.database.dao.HistoryDao
import com.alxdev.two.moneychanger.datax.datasource.entity.Currency
import com.alxdev.two.moneychanger.datax.datasource.entity.History

@Database(entities = [Currency::class, History::class], version = 4, exportSchema = false)
abstract class MoneyChangerDataBase : RoomDatabase() {

    abstract val currencyDAO: CurrencyDao
    abstract val historyDao: HistoryDao

    companion object {

        private const val DB_NAME: String = "money_changer_database"

        @Volatile
        private var INSTANCE: MoneyChangerDataBase? = null

        fun getInstance(context: Context): MoneyChangerDataBase {
            synchronized(this) {

                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MoneyChangerDataBase::class.java,
                        DB_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}