package com.alxdev.two.moneychanger.datax.datasource.network.contractimp

import com.alxdev.two.moneychanger.dataold.remote.Constants
import com.alxdev.two.moneychanger.dataold.remote.CurrencyAPIService
import com.alxdev.two.moneychanger.datasourcex.currency.ContryDTO
import com.alxdev.two.moneychanger.datax.datasource.contract.CountryAPIAction
import ru.gildor.coroutines.retrofit.Result
import ru.gildor.coroutines.retrofit.awaitResult
import javax.inject.Inject

class CountryAPIActionImpl @Inject constructor(
    private val currencyClient: CurrencyAPIService,
) : CountryAPIAction {
    override suspend fun getCountryResult(): Result<ContryDTO> {
        return currencyClient.getCurrencyResult(Constants.Key.ACCESS_KEY).awaitResult()
    }
}