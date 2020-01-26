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
import com.alxdev.two.moneychanger.ui.changer.CurrencyInformationDTO
import com.alxdev.two.moneychanger.ui.changer.toHistory
import fr.speekha.httpmocker.MockResponseInterceptor
import fr.speekha.httpmocker.model.ResponseDescriptor
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

    private val mockClient: CurrencyAPIService by lazy {
        AppApplication.mockRetrofit.retrofitBuild(Constants.API.BASE_URL, initMockSolution())
            .create(CurrencyAPIService::class.java)
    }

    private val countryClient: CurrencyCountryAPIService by lazy {
        AppApplication.currencyCountryRetrofitBuild.create(
            CurrencyCountryAPIService::class.java
        )
    }

    private fun initMockSolution(): MockResponseInterceptor.Builder {
        return MockResponseInterceptor.Builder().apply {
            useDynamicMocks {
                ResponseDescriptor(code = 200, body = Constants.MockData.JSON)
            }
        }
    }

    suspend fun syncCurrencyAPIV2() = withContext(Dispatchers.IO) {
        Log.i("alxxt", "class 00_0 call API - ${Thread.currentThread().name}")
        when (val result = mockClient.getCurrencyResult(Constants.Key.ACCESS_KEY).awaitResult()) {
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

    private suspend fun saveCurrencyList(currencyList: List<Currency>) =
        withContext(Dispatchers.IO) {
            Log.i("alxx", "class 00_0 Elements to save > ${currencyList.size}")
            Log.i("alxxt", "class 00_0 INSERT - ${Thread.currentThread().name}")
            moneyChangerDataBase.currencyDAO.clear()
            moneyChangerDataBase.currencyDAO.saveCurrencyList(currencyList)
        }

    suspend fun saveHistory(currencyInformation: CurrencyInformationDTO) =
        withContext(Dispatchers.IO) {
            moneyChangerDataBase.historyDao.insert(currencyInformation.toHistory())
        }

    fun getCurrencyListLive(): LiveData<List<Currency>?> = runBlocking {
        Log.i("alxxt", "class 00 GET LIVE - ${Thread.currentThread().name}")
        withContext(Dispatchers.IO) {
            Log.i("alxxt", "class 00_0 GET LIVE - ${Thread.currentThread().name}")
            moneyChangerDataBase.currencyDAO.getAllLiveData()
        }
    }

    fun getHistoryListLive(): LiveData<List<History>?> {
        Log.i("alxxt", "class 00_0 GET LIVE HISTORY- ${Thread.currentThread().name}")
        return moneyChangerDataBase.historyDao.getAllLiveData()
    }

    suspend fun getCurrencyCountryListV2() = withContext(Dispatchers.IO) {
        Log.i("alxxt", "class 00 GET V2 START == - ${Thread.currentThread().name}")
        val handler = CoroutineExceptionHandler { _, exception ->
            println("Caught $exception")
        }
        try {
            val list = moneyChangerDataBase.currencyDAO.getAll()
            list?.let { _list ->
                val newList = _list.map {
                    it.description.replace("USD", "")
                }
                supervisorScope {
                    for (item in newList) {
                        launch(handler) {
                            currencyCountryInfo(item)
//                            Exception EXAMPLE -->
//                            if(item.contentEquals("BDT"))
//                                throw java.lang.Exception("BDT")

                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.i("alxxE", e.toString())
        }
        Log.i("alxxt", "class 00 GET V2 FINISH == - ${Thread.currentThread().name}")
    }

    private suspend fun currencyCountryInfo(currencyName: String) {
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
}