package com.alxdev.two.moneychanger.repo

import androidx.lifecycle.LiveData
import com.alxdev.two.moneychanger.data.local.MoneyChangerDataBase
import com.alxdev.two.moneychanger.data.local.entity.Currency
import com.alxdev.two.moneychanger.data.local.entity.History
import com.alxdev.two.moneychanger.data.remote.Constants
import com.alxdev.two.moneychanger.data.remote.CurrencyAPIService
import com.alxdev.two.moneychanger.data.remote.CurrencyCountryAPIService
import com.alxdev.two.moneychanger.data.remote.currency.CurrencyDTO
import com.alxdev.two.moneychanger.ui.changer.CurrencyInformationDTO
import com.alxdev.two.moneychanger.ui.changer.toCurrency
import com.alxdev.two.moneychanger.ui.changer.toHistory
import kotlinx.coroutines.*
import ru.gildor.coroutines.retrofit.Result
import ru.gildor.coroutines.retrofit.awaitResult
import javax.inject.Inject

class ChangerRepository @Inject constructor(
    private val moneyChangerDataBase: MoneyChangerDataBase,
    private val currencyClient: CurrencyAPIService,
    private val currencyCountryClient: CurrencyCountryAPIService,
) {

    suspend fun syncCurrencyAPI() = withContext(Dispatchers.IO) {
        when (val result =
            currencyClient.getCurrencyResult(Constants.Key.ACCESS_KEY).awaitResult()) {
            is Result.Ok -> {
                getCurrencyCountryList(result.value)
            }
            is Result.Error -> {
            }
            is Result.Exception -> {
            }
        }
    }

    private suspend fun saveCurrencyList(currencyList: List<Currency>) =
        withContext(Dispatchers.IO) {
            moneyChangerDataBase.currencyDAO.clear()
            moneyChangerDataBase.currencyDAO.saveCurrencyList(currencyList)
        }

    suspend fun saveHistory(currencyInformation: CurrencyInformationDTO) =
        withContext(Dispatchers.IO) {
            moneyChangerDataBase.historyDao.insert(currencyInformation.toHistory())
        }

    fun getCurrencyListLive(): LiveData<List<Currency>?> = runBlocking {
        withContext(Dispatchers.IO) {
            moneyChangerDataBase.currencyDAO.getAllLiveData()
        }
    }

    fun getHistoryListLive(): LiveData<List<History>?> {
        return moneyChangerDataBase.historyDao.getAllLiveData()
    }

    private suspend fun getCurrencyCountryList(currency: CurrencyDTO) {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("Caught $exception")
        }

        if (moneyChangerDataBase.currencyDAO.getCount() == 0) {
            val currencyListFromAPI = mutableListOf<Currency>()
            supervisorScope {
                currency.quotes.mapKeys {
                    it.key.replace("USD", "")
                }.toMap().forEach {
                    launch(handler) {
                        currencyCountryInfo(it)?.let {
                            currencyListFromAPI.add(it)
                        }
                    }
                }
            }

            currencyListFromAPI.takeUnless {
                it.isNullOrEmpty()
            }?.let {
                saveCurrencyList(it)
            }

        }
    }

    private suspend fun currencyCountryInfo(quote: Map.Entry<String, Double>): Currency? {
        return when (val result =
            currencyCountryClient.getCountryByCurrencyName(quote.key).awaitResult()) {
            is Result.Ok -> {
                result.value[0].toCurrency(quote)
            }
            is Result.Error -> {
                null
            }
            is Result.Exception -> {
                null
            }
        }
    }
}


