package com.alxdev.two.moneychanger.core.data.external

import com.alxdev.two.moneychanger.data.remote.Constants
import com.alxdev.two.moneychanger.data.remote.CurrencyAPIService
import com.alxdev.two.moneychanger.data.remote.currency.CurrencyDTO
import ru.gildor.coroutines.retrofit.Result
import ru.gildor.coroutines.retrofit.awaitResult
import javax.inject.Inject

class CurrencyAPIActionImpl @Inject constructor(
    private val currencyClient: CurrencyAPIService,
) : CurrencyAPIAction {
    override suspend fun getCurrencyResult(): Result<CurrencyDTO> {
        return currencyClient.getCurrencyResult(Constants.Key.ACCESS_KEY).awaitResult()
    }
}