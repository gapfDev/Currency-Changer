package com.alxdev.two.moneychanger.domainx.repocontract

import com.alxdev.two.moneychanger.datasourcex.currency.ContryDTO
import com.alxdev.two.moneychanger.datasourcex.currencycountry.CurrencyCountryDTO
import com.alxdev.two.moneychanger.domain.ResultManager
import ru.gildor.coroutines.retrofit.Result

interface ChangerExternalDataSource {
    suspend fun getCurrencyCountryAPI(
        currencyName: String
    ): ResultManager<List<CurrencyCountryDTO>>

    suspend fun callCountriesAPI(): ResultManager<ContryDTO>
    suspend fun callCountryResult(): Result<ContryDTO>
    suspend fun getCountryByCurrencyName(currencyName: String): Result<List<CurrencyCountryDTO>>
}