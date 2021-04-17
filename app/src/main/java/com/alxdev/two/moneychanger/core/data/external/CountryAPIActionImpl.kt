package com.alxdev.two.moneychanger.core.data.external

import com.alxdev.two.moneychanger.data.remote.Constants
import com.alxdev.two.moneychanger.data.remote.CurrencyAPIService
import com.alxdev.two.moneychanger.data.remote.currency.ContryDTO
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