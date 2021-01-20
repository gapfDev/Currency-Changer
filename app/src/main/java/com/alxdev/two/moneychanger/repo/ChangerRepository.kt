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
import com.alxdev.two.moneychanger.data.remote.currency.toCurrencyEntity
import com.alxdev.two.moneychanger.data.remote.currencycountry.CurrencyCountryDTO
import com.alxdev.two.moneychanger.util.extension.toCurrencyEntity
import com.alxdev.two.moneychanger.util.extension.toHistoryEntity
import com.alxdev.two.moneychanger.util.extension.toModel
import com.alxdev.two.moneychanger.util.extension.toModelCurrency

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

    suspend fun saveCurrencyCountryList(currencyCountryList: List<Currency>) {
        currencyCountryList.takeUnless {
            it.isNullOrEmpty()
        }?.let { _currencyList ->
            currencyDAOImp.cleanAndSaveCurrencyList(_currencyList)
        }
    }


    suspend fun callCurrencyAPI(action: suspend (CurrencyDTO) -> Unit) {
        UtilityNetworkWorker.processAPIResult(
            { currencyAPIActionImp.getCurrencyResult() },
            { _currencyDTO ->
                if (currencyDAOImp.getCount() == 0)
                    action(_currencyDTO)
            }
        )
    }

    suspend fun syncCurrencyCountryAPI(currencyDTO: CurrencyDTO) {
        saveCurrencyCountryList(
            currencyDTO.toCurrencyEntity { _currencyName, _currencyValue ->
                getCurrencyCountryAPI(_currencyName)?.toCurrencyEntity(_currencyValue)
            }
        )
    }

    private suspend fun getCurrencyCountryAPI(
        currencyName: String
    ): CurrencyCountryDTO? =
        UtilityNetworkWorker.getResultManagerFromAPIResult {
            currencyCountryAPIActionImpl.getCountryByCurrencyName(currencyName)
        }.result?.get(0)


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






