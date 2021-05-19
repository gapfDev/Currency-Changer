package com.alxdev.two.moneychanger.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.alxdev.two.moneychanger.Constant
import com.alxdev.two.moneychanger.core.UtilityNetworkWorker
import com.alxdev.two.moneychanger.core.dataimp.external.CountryAPIAction
import com.alxdev.two.moneychanger.core.dataimp.external.CurrencyCountryAPIAction
import com.alxdev.two.moneychanger.core.dataimp.local.CurrencyDAOAction
import com.alxdev.two.moneychanger.core.dataimp.local.HistoryDAOAction
import com.alxdev.two.moneychanger.data.local.entity.Currency
import com.alxdev.two.moneychanger.data.model.CurrencyInformation
import com.alxdev.two.moneychanger.data.model.History
import com.alxdev.two.moneychanger.data.remote.currency.ContryDTO
import com.alxdev.two.moneychanger.data.remote.currencycountry.CurrencyCountryDTO
import com.alxdev.two.moneychanger.util.extension.toHistoryEntity
import com.alxdev.two.moneychanger.util.extension.toModel
import com.alxdev.two.moneychanger.util.extension.toModelCurrency
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class ChangerRepository(
    private val historyDAOImp: HistoryDAOAction,
    private val currencyDAOImp: CurrencyDAOAction,
    private val countryAPIActionImp: CountryAPIAction,
    private val currencyCountryAPIActionImpl: CurrencyCountryAPIAction,
) {


    private val _historyLiveData = MediatorLiveData<List<History>>()
    val historyLiveData: LiveData<List<History>> = _historyLiveData

    val currencyList: LiveData<List<com.alxdev.two.moneychanger.data.model.Currency>> =
        Transformations.map(currencyDAOImp.getAllOrderByAsc()) {
            val elementList = it?.toModelCurrency()
            return@map if (elementList.isNullOrEmpty()) {
                listOf(Constant.ModelDefault.currencyModel)
            } else {
                elementList
            }
        }

    init {
        _historyLiveData.addSource(historyDAOImp.getHistoryByDesc()) {
            it?.takeUnless { _list ->
                _list.isNullOrEmpty()
            }?.let {
                _historyLiveData.value = it.toModel()
            }
        }
    }

    suspend fun saveHistory(currencyInformation: CurrencyInformation) =
        historyDAOImp.setHistory(currencyInformation.toHistoryEntity())

    suspend fun saveCurrencyCountryList(currencyCountryList: List<Currency>) {
        currencyCountryList.takeUnless {
            it.isNullOrEmpty()
        }?.let { _currencyList ->
            currencyDAOImp.cleanAndSaveCurrencyList(_currencyList)
        }
    }

    suspend fun callCountriesAPI(action: suspend (ContryDTO) -> Unit) {
        UtilityNetworkWorker.processAPIResult(
            { countryAPIActionImp.getCountryResult() },
            { _currencyDTO ->
                if (currencyDAOImp.getCount() == 0)
                    action(_currencyDTO)
            }
        )
    }

    suspend fun getCurrencyCountryAPI(
        currencyName: String
    ): List<CurrencyCountryDTO>? =
        UtilityNetworkWorker.getResultManagerFromAPIResult {
            currencyCountryAPIActionImpl.getCountryByCurrencyName(currencyName)
        }.result

    suspend fun getCurrencyList(
        countryMap: Map<String, Double>,
        action: suspend (String, Double) -> Currency?
    ): List<Currency> {
        val currencyCountryList = mutableListOf<Currency>()

        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            println("Caught $exception")
        }

        supervisorScope {
            for ((key, value) in countryMap) {
                launch(Dispatchers.IO + exceptionHandler) {
                    action(key, value).let { _currencyEntity ->
                        _currencyEntity?.let { currencyCountryList.add(it) }
                    }
                }
            }
        }
        return currencyCountryList
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






