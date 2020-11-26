package com.alxdev.two.moneychanger.core.data.local

import androidx.lifecycle.LiveData
import com.alxdev.two.moneychanger.data.local.MoneyChangerDataBase
import com.alxdev.two.moneychanger.data.local.entity.Currency
import javax.inject.Inject

class CurrencyDAOImp @Inject constructor(
    private val moneyChangerDataBase: MoneyChangerDataBase,
) : CurrencyDAOAction{
    override suspend fun clear() = moneyChangerDataBase.currencyDAO.clear()

    override suspend fun saveCurrencyList(currencyList: List<Currency>) = moneyChangerDataBase.currencyDAO.saveCurrencyList(currencyList)

    override fun getAllOrderByAsc(): LiveData<List<Currency>?> = moneyChangerDataBase.currencyDAO.getAllLiveData()

    override suspend fun getCount(): Int = moneyChangerDataBase.currencyDAO.getCount()
}