package com.alxdev.two.moneychanger.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alxdev.two.moneychanger.data.local.dao.CurrencyDao
import com.alxdev.two.moneychanger.data.local.entity.Currency

@Database(entities = [Currency::class], version = 1, exportSchema = false)
abstract class MoneyChangerDataBase : RoomDatabase() {

    abstract val currencyDAO: CurrencyDao

    companion object {

        private const val DB_NAME: String = "money_changer_database"

        @Volatile
        private var INSTANCE: MoneyChangerDataBase? = null

        fun getInstance(context: Context): MoneyChangerDataBase {
            synchronized(this) {

                var instance =
                    INSTANCE

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