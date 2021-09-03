package com.alxdev.two.moneychanger.datax.datasource.contract

import com.alxdev.two.moneychanger.datasourcex.currencycountry.CurrencyCountryDTO
import ru.gildor.coroutines.retrofit.Result

interface CurrencyCountryAPIAction {
    suspend fun getCountryByCurrencyName(currencyName: String): Result<List<CurrencyCountryDTO>>
}