package com.alxdev.two.moneychanger.repo

import android.util.Log
import androidx.lifecycle.LiveData
import com.alxdev.two.moneychanger.AppApplication
import com.alxdev.two.moneychanger.data.local.MoneyChangerDataBase
import com.alxdev.two.moneychanger.data.local.entity.Currency
import com.alxdev.two.moneychanger.data.local.entity.History
import com.alxdev.two.moneychanger.data.remote.Constants
import com.alxdev.two.moneychanger.data.remote.CurrencyAPIService
import com.alxdev.two.moneychanger.data.remote.CurrencyCountryAPIService
import com.alxdev.two.moneychanger.ui.changer.CurrencyInformation
import kotlinx.coroutines.*
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
        AppApplication.currencyListRetrofitBuild.retrofit.create(
            CurrencyAPIService::class.java
        )
    }

    private val countryClient: CurrencyCountryAPIService by lazy {
        AppApplication.currencyCountryRetrofitBuild.retrofit.create(
            CurrencyCountryAPIService::class.java
        )
    }

    suspend fun getCurrencyAPI() {
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

    fun getCurrencyListLive(): LiveData<List<Currency>?> {
        Log.i("alxxt", "class 00 GET LIVE - ${Thread.currentThread().name}")
        return moneyChangerDataBase.currencyDAO.getAllLiveData()
    }


    suspend fun getCurrencyListV2() = withContext(Dispatchers.IO) {
        Log.i("alxxt", "class 00 GET V2 START == - ${Thread.currentThread().name}")
//        val jobs = mutableListOf<Job>()
        val sup = SupervisorJob()
        val handler = CoroutineExceptionHandler { _, exception ->
            println("Caught $exception")
        }

        try {

            supervisorScope {
                //            coroutineScope {
                val list = moneyChangerDataBase.currencyDAO.getAll()
                list?.let { _list ->
                    for ((index, item) in _list.withIndex()) {
                        if (index < 3) {

                            launch {
                                val currencyName = item.description.replace("USD", "")

//                                if (index == 1)
//                                    throw Exception("Test alxx")
                                Log.i(
                                    "alxxt",
                                    "class 00_0 GET CURRENCY_COUNTRY START == $currencyName - ${Thread.currentThread().name}"
                                )
                                currencyCountryInfo(currencyName)
                                Log.i(
                                    "alxxt",
                                    "class 00_0 GET CURRENCY_COUNTRY END == $currencyName - ${Thread.currentThread().name}"
                                )
                            }
                        }
                    }
                }
            }


//            jobs.forEach {
//                it.join()
//            }


        } catch (e: Exception) {
            Log.i("alxxE", e.toString())
        }

        Log.i("alxxt", "class 00 GET V2 FINISH == - ${Thread.currentThread().name}")
    }


    private suspend fun currencyCountryInfo(currencyName: String) {

        delay(5_000)
        when (val result = countryClient.getCountryByCurrencyName(currencyName).awaitResult()) {
            is Result.Ok -> {
                Log.i(
                    "alxxt",
                    "class 000_0 call API > OK ${result.value[0].name} - ${Thread.currentThread().name}"
                )
            }
            is Result.Error -> {
                Log.i(
                    "alxxt",
                    "class 000_0 call API > ERROR ${result.exception} - ${Thread.currentThread().name}"
                )
            }
            is Result.Exception -> {
                Log.i(
                    "alxxt",
                    "class 000_0 call API > EXCEPTION ${result.exception} - ${Thread.currentThread().name}"
                )
            }
        }

    }

    fun getHistoryListLive(): LiveData<List<History>?> {
        Log.i("alxxt", "class 00_0 GET LIVE HISTORY- ${Thread.currentThread().name}")
        return moneyChangerDataBase.historyDao.getAllLiveData()
    }

    suspend fun saveHistory(currencyInformation: CurrencyInformation) =
        withContext(Dispatchers.IO) {
            moneyChangerDataBase.historyDao.insert(currencyInformation.toHistory())
        }

    private fun CurrencyInformation.toHistory() = History(
        localCountry = localCountry,
        foreignCountry = foreignCountry,
        localCurrencyQuantity = localCurrencyQuantity,
        foreignCurrencyQuantity = foreignCurrencyQuantity
    )
}

object Coroutines {
    fun io(work: suspend (() -> Unit)): Job =
        CoroutineScope(Dispatchers.IO).launch {
            work()
        }

    fun <T : Any> ioThenMain(work: suspend (() -> T?), callback: ((T?) -> Unit)): Job =
        CoroutineScope(Dispatchers.Main).launch {
            val data = CoroutineScope(Dispatchers.IO).async rt@{
                return@rt work()
            }.await()
            callback(data)
        }
}