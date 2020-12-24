package com.alxdev.two.moneychanger.core.data.local

import androidx.lifecycle.LiveData
import com.alxdev.two.moneychanger.data.local.entity.Currency

interface CurrencyDAOAction {
    suspend fun clear()
    suspend fun saveCurrencyList(currencyList: List<Currency>)
    suspend fun cleanAndSaveCurrencyList(currencyList: List<Currency>)
    fun getAllOrderByAsc(): LiveData<List<Currency>?>
    suspend fun getCount(): Int

}