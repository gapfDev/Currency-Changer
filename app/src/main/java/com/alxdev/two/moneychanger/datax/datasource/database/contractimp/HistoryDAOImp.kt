package com.alxdev.two.moneychanger.datax.datasource.database.contractimp

import androidx.lifecycle.LiveData
import com.alxdev.two.moneychanger.datax.datasource.database.MoneyChangerDataBase
import com.alxdev.two.moneychanger.datax.datasource.entity.History
import com.alxdev.two.moneychanger.datax.datasource.contract.HistoryDAOAction
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