package com.alxdev.two.moneychanger.datax.datasource.network.contractimp

import com.alxdev.two.moneychanger.dataold.remote.CurrencyCountryAPIService
import com.alxdev.two.moneychanger.datasourcex.currencycountry.CurrencyCountryDTO
import com.alxdev.two.moneychanger.datax.datasource.contract.CurrencyCountryAPIAction
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