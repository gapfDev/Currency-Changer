package com.alxdev.two.moneychanger.domainx.repocontract

import androidx.lifecycle.LiveData
import com.alxdev.two.moneychanger.datasourcex.currency.ContryDTO
import com.alxdev.two.moneychanger.datasourcex.currencycountry.CurrencyCountryDTO
import com.alxdev.two.moneychanger.domainx.model.Currency
import com.alxdev.two.moneychanger.domainx.model.CurrencyInformation
import com.alxdev.two.moneychanger.domainx.model.History

interface ChangerRepository {
    val historyLiveData: LiveData<List<History>>
    val currencyList: LiveData<List<Currency>>
    suspend fun saveHistory(currencyInformation: CurrencyInformation)
    suspend fun callCurrencyCountryAPI(
        currencyName: String,
    ): List<CurrencyCountryDTO>?
    fun getDefaultCurrency(): List<Currency>
    suspend fun syncDataSource()
    suspend fun callCurrencyAPI(action: suspend (ContryDTO) -> Unit)
}