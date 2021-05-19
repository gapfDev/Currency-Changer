package com.alxdev.two.moneychanger.core.dataimp.local

import androidx.lifecycle.LiveData
import com.alxdev.two.moneychanger.data.local.MoneyChangerDataBase
import com.alxdev.two.moneychanger.data.local.entity.History
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HistoryDAOImp @Inject constructor(
    private val moneyChangerDataBase: MoneyChangerDataBase,
) : HistoryDAOAction {
    override fun getHistoryByDesc(): LiveData<List<History>?> =
        moneyChangerDataBase.historyDao.getAllLiveData()

    override suspend fun setHistory(history: History) {
        withContext(Dispatchers.IO) {
            moneyChangerDataBase.historyDao.insert(history)
        }
    }
}