package com.alxdev.two.moneychanger.datax.changer.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.alxdev.two.moneychanger.Constant
import com.alxdev.two.moneychanger.datasourcex.currency.ContryDTO
import com.alxdev.two.moneychanger.datasourcex.currencycountry.CurrencyCountryDTO
import com.alxdev.two.moneychanger.datax.datasource.entity.Currency
import com.alxdev.two.moneychanger.datax.datasource.mapper.toCurrencyEntity
import com.alxdev.two.moneychanger.datax.datasource.mapper.toHistoryEntity
import com.alxdev.two.moneychanger.datax.datasource.util.cleanCurrencyNameMap
import com.alxdev.two.moneychanger.datax.util.Worker
import com.alxdev.two.moneychanger.domain.UtilityNetworkWorker
import com.alxdev.two.moneychanger.domainx.model.CurrencyInformation
import com.alxdev.two.moneychanger.domainx.model.History
import com.alxdev.two.moneychanger.domainx.repocontract.ChangerExternalDataSource
import com.alxdev.two.moneychanger.domainx.repocontract.ChangerLocalDataSource
import com.alxdev.two.moneychanger.domainx.repocontract.ChangerRepository
import com.alxdev.two.moneychanger.util.extension.toModel
import com.alxdev.two.moneychanger.util.extension.toModelCurrency

class ChangerRepositoryImp(
    private val changerLocalDataSource: ChangerLocalDataSource,
    private val changerExternalDataSource: ChangerExternalDataSource,
) : ChangerRepository {

    private val _historyLiveData = MediatorLiveData<List<History>>()
    override val historyLiveData: LiveData<List<History>> = _historyLiveData

    override val currencyList: LiveData<List<com.alxdev.two.moneychanger.domainx.model.Currency>> =
        Transformations.map(changerLocalDataSource.getAllOrderByAsc()) {
            val elementList = it?.toModelCurrency()
            return@map if (elementList.isNullOrEmpty()) {
                listOf(Constant.ModelDefault.currencyModel)
            } else {
                elementList
            }
        }

    init {
        _historyLiveData.addSource(changerLocalDataSource.getHistoryByDesc()) {
            it?.takeUnless { _list ->
                _list.isNullOrEmpty()
            }?.let {
                _historyLiveData.value = it.toModel()
            }
        }
    }

    override suspend fun callCurrencyCountryAPI(
        currencyName: String,
    ): List<CurrencyCountryDTO>? {
        return UtilityNetworkWorker.getResultManagerFromAPIResult {
            changerExternalDataSource.getCountryByCurrencyName(currencyName)
        }.result
    }

    override suspend fun callCurrencyAPI(action: suspend (ContryDTO) -> Unit) {
        UtilityNetworkWorker.processAPIResult(
            { changerExternalDataSource.callCountryResult() },
            { action(it) })
    }

    override fun getDefaultCurrency(): List<com.alxdev.two.moneychanger.domainx.model.Currency> {
        val elementList = getRawDefaultCurrency()
        return if (elementList.isNullOrEmpty()) {
            listOf(Constant.ModelDefault.currencyModel)
        } else {
            elementList
        }
    }

    override suspend fun syncDataSource() {
        callCurrencyAPI { _countryDTO ->
            if (changerLocalDataSource.getCount() == 0)
                Worker.getCurrencyList(_countryDTO.cleanCurrencyNameMap()) { _currencyName, _currencyValue ->
                    callCurrencyCountryAPI(_currencyName).toCurrencyEntity(_currencyValue)
                }.also {
                    saveCurrencyCountryList(it)
                }
        }
    }

    override suspend fun saveHistory(currencyInformation: CurrencyInformation) =
        changerLocalDataSource.setHistory(currencyInformation.toHistoryEntity())

    private suspend fun saveCurrencyCountryList(currencyCountryList: List<Currency>) {
        currencyCountryList.takeUnless {
            it.isNullOrEmpty()
        }?.let { _currencyList ->
            changerLocalDataSource.cleanAndSaveCurrencyList(_currencyList)
        }
    }

    private fun getRawDefaultCurrency(): List<com.alxdev.two.moneychanger.domainx.model.Currency> {
        return listOf(Constant.ModelDefault.currencyModel)
    }
}





