package com.alxdev.two.moneychanger.core.data.local

import androidx.lifecycle.LiveData
import com.alxdev.two.moneychanger.data.local.MoneyChangerDataBase
import com.alxdev.two.moneychanger.data.local.entity.History
import javax.inject.Inject

class HistoryDAOImp @Inject constructor(
    private val moneyChangerDataBase: MoneyChangerDataBase,
) : HistoryDAOAction {
    override fun getHistoryByDesc(): LiveData<List<History>?> =
        moneyChangerDataBase.historyDao.getAllLiveData()

    override suspend fun setHistory(history: History) {
        moneyChangerDataBase.historyDao.insert(history)
    }
}