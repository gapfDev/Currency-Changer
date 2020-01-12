package com.alxdev.two.moneychanger.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.alxdev.two.moneychanger.data.local.entity.Currency

@Dao
interface CurrencyDao {

    @Insert
    fun insert(currency: Currency)

    @Insert
    fun insertCurrencyList(currencyList: List<Currency>)

    @Update
    fun update(currency: Currency)

    @Delete
    fun delete(currency: Currency)

    @Query("DELETE FROM " + Currency.TABLE_NAME)
    fun clear()

    @Query("SELECT * FROM " + Currency.TABLE_NAME)
    fun getAllLiveData(): LiveData<List<Currency>?>

    @Query("SELECT * FROM " + Currency.TABLE_NAME)
    fun getAll(): List<Currency>?

    @Query("SELECT * FROM ${Currency.TABLE_NAME} LIMIT 1 ")
    fun getCurrency(): Currency?

    @Query("SELECT * FROM ${Currency.TABLE_NAME} LIMIT 1 ")
    fun getCurrencyLD(): LiveData<Currency?>
}