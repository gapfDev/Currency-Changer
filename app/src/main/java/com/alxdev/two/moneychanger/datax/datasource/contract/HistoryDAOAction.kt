package com.alxdev.two.moneychanger.datax.datasource.contract

import androidx.lifecycle.LiveData
import com.alxdev.two.moneychanger.datax.datasource.entity.History

interface HistoryDAOAction {

    fun getHistoryByDesc() : LiveData<List<History>?>
    suspend fun setHistory(history: History)
}