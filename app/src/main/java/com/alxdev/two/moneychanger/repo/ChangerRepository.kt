package com.alxdev.two.moneychanger.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.alxdev.two.moneychanger.Constant
import com.alxdev.two.moneychanger.core.UtilityNetworkWorker
import com.alxdev.two.moneychanger.core.data.external.CurrencyAPIAction
import com.alxdev.two.moneychanger.core.data.external.CurrencyCountryAPIAction
import com.alxdev.two.moneychanger.core.data.local.CurrencyDAOAction
import com.alxdev.two.moneychanger.core.data.local.HistoryDAOAction
import com.alxdev.two.moneychanger.data.local.entity.Currency
import com.alxdev.two.moneychanger.data.model.CurrencyInformation
import com.alxdev.two.moneychanger.data.model.History
import com.alxdev.two.moneychanger.data.remote.currency.CurrencyDTO
import com.alxdev.two.moneychanger.util.extension.toCurrencyEntity
import com.alxdev.two.moneychanger.util.extension.toHistoryEntity
import com.alxdev.two.moneychanger.util.extension.toModel
import com.alxdev.two.moneychanger.util.extension.toModelCurrency
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class ChangerRepository(
    private val historyDAOImp: HistoryDAOAction,
    private val currencyDAOImp: CurrencyDAOAction,
    private val currencyAPIActionImp: CurrencyAPIAction,
    private val currencyCountryAPIActionImpl: CurrencyCountryAPIAction,
) {
    private val _mediatorHistory =
        MediatorLiveData<List<History>>()
    val historyLiveData: LiveData<List<History>> =
        _mediatorHistory

    private val _mediatorCurrencyList =
        MediatorLiveData<List<com.alxdev.two.moneychanger.data.model.Currency>>().apply {
            value = listOf(Constant.ModelDefault.currencyModel)
        }
    val rawCurrencyList: LiveData<List<com.alxdev.two.moneychanger.data.model.Currency>> =
        _mediatorCurrencyList
    val currencyList: LiveData<List<com.alxdev.two.moneychanger.data.model.Currency>> =
        Transformations.map(rawCurrencyList) {
            val elementList = it
            return@map if (elementList.isNullOrEmpty()) {
                listOf(Constant.ModelDefault.currencyModel)
            } else {
                elementList
            }
        }

    init {
        initMediator()
    }

    private fun initMediator() {
        _mediatorHistory.addSource(historyDAOImp.getHistoryByDesc()) { _entityHistoryList ->
            _entityHistoryList?.takeUnless { _list ->
                _list.isNullOrEmpty()
            }?.let { _list ->
                _mediatorHistory.value = _list.toModel()
            }
        }

        _mediatorCurrencyList.addSource(currencyDAOImp.getAllOrderByAsc()) { _currencyList ->
            this._mediatorCurrencyList.value = _currencyList?.toModelCurrency()
        }
    }

    suspend fun saveHistory(currencyInformation: CurrencyInformation) =
        historyDAOImp.setHistory(currencyInformation.toHistoryEntity())

//    fun getCurrencyListLive(): LiveData<List<com.alxdev.two.moneychanger.data.model.Currency>?> =
//        currencyDAOImp.getAllOrderByAsc().toModelCurrency()

    private suspend fun saveCurrencyCountryList(currency: CurrencyDTO) {
        getCurrencyCountryList(currency).takeUnless {
            it.isNullOrEmpty()
        }?.let { _currencyList ->
            currencyDAOImp.cleanAndSaveCurrencyList(_currencyList)
        }
    }

    private suspend fun getCurrencyCountryList(currency: CurrencyDTO): List<Currency> {
        val currencyCountryList = mutableListOf<Currency>()

        if (currencyDAOImp.getCount() == 0) {
            val exceptionHandler = CoroutineExceptionHandler { _, exception ->
                println("Caught $exception")
            }

            supervisorScope {
                currency.getCurrencyNameMap().forEach { _entry ->
                    launch(exceptionHandler) {
                        getCurrencyCountryInfo(_entry)?.let { _currencyCountry ->
                            currencyCountryList.add(_currencyCountry)
                        }
                    }
                }
            }
        }
        return currencyCountryList
    }

    suspend fun syncCurrencyAPI() {
        UtilityNetworkWorker.processAPIResult(
            { currencyAPIActionImp.getCurrencyResult() },
            { currencyDTO -> saveCurrencyCountryList(currencyDTO) }
        )
    }

    private suspend fun getCurrencyCountryInfo(quote: Map.Entry<String, Double>): Currency? {
        return UtilityNetworkWorker.getResultManagerFromAPIResult {
            currencyCountryAPIActionImpl.getCountryByCurrencyName(quote.key)
        }.result?.get(0)?.toCurrencyEntity(quote)
    }

    private fun getRawDefaultCurrency(): List<com.alxdev.two.moneychanger.data.model.Currency> {
        return listOf(Constant.ModelDefault.currencyModel)
    }

    fun getDefaultCurrency(): List<com.alxdev.two.moneychanger.data.model.Currency> {
        val elementList = getRawDefaultCurrency()
        return if (elementList.isNullOrEmpty()) {
            listOf(Constant.ModelDefault.currencyModel)
        } else {
            elementList
        }
    }
}






