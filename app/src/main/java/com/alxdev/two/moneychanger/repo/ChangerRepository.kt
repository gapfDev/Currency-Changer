package com.alxdev.two.moneychanger.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.alxdev.two.moneychanger.data.local.MoneyChangerDataBase
import com.alxdev.two.moneychanger.data.local.entity.Currency
import com.alxdev.two.moneychanger.data.model.CurrencyInformation
import com.alxdev.two.moneychanger.data.remote.Constants
import com.alxdev.two.moneychanger.data.remote.CurrencyAPIService
import com.alxdev.two.moneychanger.data.remote.CurrencyCountryAPIService
import com.alxdev.two.moneychanger.data.remote.currency.CurrencyDTO
import com.alxdev.two.moneychanger.extension.toCurrencyEntity
import com.alxdev.two.moneychanger.extension.toHistoryEntity
import com.alxdev.two.moneychanger.extension.toModel
import kotlinx.coroutines.*
import ru.gildor.coroutines.retrofit.Result
import ru.gildor.coroutines.retrofit.awaitResult
import javax.inject.Inject

class ChangerRepository @Inject constructor(
    private val moneyChangerDataBase: MoneyChangerDataBase,
    private val currencyClient: CurrencyAPIService,
    private val currencyCountryClient: CurrencyCountryAPIService,
) {

    private val _historyMediator =
        MediatorLiveData<List<com.alxdev.two.moneychanger.data.model.History>>()
    val historyLiveData: LiveData<List<com.alxdev.two.moneychanger.data.model.History>> =
        _historyMediator

    init {
        _historyMediator.addSource(moneyChangerDataBase.historyDao.getAllLiveData()) { _entityHistory ->
            _entityHistory?.let { _entityHistoryList ->
                _historyMediator.value = _entityHistoryList.toModel()
            }
        }
    }

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

    suspend fun saveHistory(currencyInformation: CurrencyInformation) =
        withContext(Dispatchers.IO) {
            moneyChangerDataBase.historyDao.insert(currencyInformation.toHistoryEntity())
        }

    fun getCurrencyListLive(): LiveData<List<Currency>?> = moneyChangerDataBase.currencyDAO.getAllLiveData()

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
                result.value[0].toCurrencyEntity(quote)
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




