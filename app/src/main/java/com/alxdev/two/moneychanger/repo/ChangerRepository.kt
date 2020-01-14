package com.alxdev.two.moneychanger.repo

import android.util.Log
import androidx.lifecycle.LiveData
import com.alxdev.two.moneychanger.AppApplication
import com.alxdev.two.moneychanger.data.local.MoneyChangerDataBase
import com.alxdev.two.moneychanger.data.local.entity.Currency
import com.alxdev.two.moneychanger.data.local.entity.History
import com.alxdev.two.moneychanger.data.remote.Constants
import com.alxdev.two.moneychanger.data.remote.CurrencyAPIService
import com.alxdev.two.moneychanger.ui.changer.CurrencyInformation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ru.gildor.coroutines.retrofit.Result
import ru.gildor.coroutines.retrofit.awaitResult

class ChangerRepository private constructor(private val moneyChangerDataBase: MoneyChangerDataBase) {

    companion object {
        @Volatile
        private var INSTANCE: ChangerRepository? = null

        fun getInstance(moneyChangerDataBase: MoneyChangerDataBase): ChangerRepository {
            synchronized(this) {
                var instance =
                    INSTANCE

                if (instance == null) {
                    instance =
                        ChangerRepository(
                            moneyChangerDataBase
                        )
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    private val client: CurrencyAPIService by lazy {
        AppApplication.retrofitBuild.retrofit.create(
            CurrencyAPIService::class.java
        )
    }

    suspend fun getCurrencyAPI() = withContext(Dispatchers.IO) {
        client.getCurrency(Constants.Key.ACCESS_KEY)
    }

    suspend fun syncCurrencyAPI() = withContext(Dispatchers.IO) {
        Log.i("alxxt", "class 00_0 call API - ${Thread.currentThread().name}")
        when (val result = client.getCurrencyResult(Constants.Key.ACCESS_KEY).awaitResult()) {
            is Result.Ok -> {
                result.value.getQuotesList().takeIf {
                    it.isNotEmpty()
                }?.let {
                    saveCurrencyList(it)
                }
            }
            is Result.Error -> {
            }
            is Result.Exception -> {
            }
        }
    }

    private suspend fun insertCurrency(currency: Currency) = withContext(Dispatchers.IO) {
        Log.i("alxx", "class 00_0 Element to save > ${currency.value}")
        moneyChangerDataBase.currencyDAO.insert(currency)
    }

    suspend fun saveCurrencyList(currencyList: List<Currency>) = withContext(Dispatchers.IO) {
        Log.i("alxx", "class 00_0 Elements to save > ${currencyList.size}")
        Log.i("alxxt", "class 00_0 INSERT - ${Thread.currentThread().name}")
        moneyChangerDataBase.currencyDAO.clear()
        moneyChangerDataBase.currencyDAO.saveCurrencyList(currencyList)
    }

    suspend fun getCurrency(): Currency? = withContext(Dispatchers.IO) {
        val data = moneyChangerDataBase.currencyDAO.getAll()?.get(0)
        Log.i(
            "alxx",
            "class 00_0 get data <- ${data?.description} ${data?.value} ${moneyChangerDataBase.currencyDAO.getAll()?.size}"
        )
        data
    }

    suspend fun getCurrencyList(): List<Currency> = withContext(Dispatchers.IO) {
        moneyChangerDataBase.currencyDAO.getAll()
    } ?: emptyList()

    fun getCurrencyListLive(): LiveData<List<Currency>?> = runBlocking {
        Log.i("alxxt", "class 00 GET LIVE - ${Thread.currentThread().name}")
        withContext(Dispatchers.IO) {
            Log.i("alxxt", "class 00_0 GET LIVE - ${Thread.currentThread().name}")
            moneyChangerDataBase.currencyDAO.getAllLiveData()
        }
    }

    fun getHistoryListLive(): LiveData<List<History>?> = runBlocking {
        Log.i("alxxt", "class 00 GET LIVE HISTORY - ${Thread.currentThread().name}")
        withContext(Dispatchers.IO) {
            Log.i("alxxt", "class 00_0 GET LIVE HISTORY- ${Thread.currentThread().name}")
            moneyChangerDataBase.historyDao.getAllLiveData()
        }

    }

    fun saveHistory(currencyInformation: CurrencyInformation) = runBlocking {
        withContext(Dispatchers.IO) {
            moneyChangerDataBase.historyDao.insert(currencyInformation.toHistory())
        }
    }

    private fun CurrencyInformation.toHistory() = History(
        localCountry = localCountry,
        foreignCountry = foreignCountry,
        localCurrencyQuantity = localCurrencyQuantity,
        foreignCurrencyQuantity = foreignCurrencyQuantity
    )
}