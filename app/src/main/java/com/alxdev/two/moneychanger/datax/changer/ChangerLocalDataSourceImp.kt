package com.alxdev.two.moneychanger.datax.changer

import androidx.lifecycle.LiveData
import com.alxdev.two.moneychanger.datax.datasource.contract.CurrencyDAOAction
import com.alxdev.two.moneychanger.datax.datasource.contract.HistoryDAOAction
import com.alxdev.two.moneychanger.datax.datasource.entity.Currency
import com.alxdev.two.moneychanger.datax.datasource.entity.History
import com.alxdev.two.moneychanger.domainx.repocontract.ChangerLocalDataSource
import javax.inject.Inject

class ChangerLocalDataSourceImp @Inject constructor(
    private val historyDAOImp: HistoryDAOAction,
    private val currencyDAOImp: CurrencyDAOAction,
) : ChangerLocalDataSource {

    override suspend fun setHistory(history: History) {
        historyDAOImp.setHistory(history)
    }

    override fun getHistoryByDesc(): LiveData<List<History>?> =
        historyDAOImp.getHistoryByDesc()

    override suspend fun clear() = currencyDAOImp.clear()

    override suspend fun saveCurrencyList(currencyList: List<Currency>) =
        currencyDAOImp.saveCurrencyList(currencyList)

    override suspend fun cleanAndSaveCurrencyList(currencyList: List<Currency>) =
        currencyDAOImp.cleanAndSaveCurrencyList(currencyList)

    override fun getAllOrderByAsc(): LiveData<List<Currency>?> = currencyDAOImp.getAllOrderByAsc()

    override suspend fun getCount(): Int = currencyDAOImp.getCount()

    override suspend fun isCurrencyCountryEmpty(): Boolean = currencyDAOImp.isCurrencyCountryEmpty()
}