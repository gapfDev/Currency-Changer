package com.alxdev.two.moneychanger.core.dataimp.external

import com.alxdev.two.moneychanger.data.remote.CurrencyCountryAPIService
import com.alxdev.two.moneychanger.data.remote.currencycountry.CurrencyCountryDTO
import ru.gildor.coroutines.retrofit.Result
import ru.gildor.coroutines.retrofit.awaitResult
import javax.inject.Inject

class CurrencyCountryAPIActionImpl @Inject constructor(
    private val currencyCountryClient: CurrencyCountryAPIService,
) : CurrencyCountryAPIAction {
    override suspend fun getCountryByCurrencyName(currencyName: String): Result<List<CurrencyCountryDTO>> {
        return currencyCountryClient.getCountryByCurrencyName(currencyName).awaitResult()
    }
}