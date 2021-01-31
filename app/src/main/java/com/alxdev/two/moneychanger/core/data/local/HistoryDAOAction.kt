package com.alxdev.two.moneychanger.core.data.local

import androidx.lifecycle.LiveData
import com.alxdev.two.moneychanger.data.local.entity.History

interface HistoryDAOAction {

    fun getHistoryByDesc() : LiveData<List<History>?>
    suspend fun setHistory(history: History)
}