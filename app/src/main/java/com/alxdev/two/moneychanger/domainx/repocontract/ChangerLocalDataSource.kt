package com.alxdev.two.moneychanger.domainx.repocontract

import androidx.lifecycle.LiveData
import com.alxdev.two.moneychanger.datax.datasource.entity.Currency

interface ChangerLocalDataSource {
    suspend fun clear()
    suspend fun saveCurrencyList(currencyList: List<Currency>)
    suspend fun cleanAndSaveCurrencyList(currencyList: List<Currency>)
    fun getAllOrderByAsc(): LiveData<List<Currency>?>
    suspend fun getCount(): Int
    suspend fun isCurrencyCountryEmpty(): Boolean
    suspend fun setHistory(history: com.alxdev.two.moneychanger.datax.datasource.entity.History)
    fun getHistoryByDesc(): LiveData<List<com.alxdev.two.moneychanger.datax.datasource.entity.History>?>
}