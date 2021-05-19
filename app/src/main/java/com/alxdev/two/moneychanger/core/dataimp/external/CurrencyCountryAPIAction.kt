package com.alxdev.two.moneychanger.core.dataimp.external

import com.alxdev.two.moneychanger.data.remote.currencycountry.CurrencyCountryDTO
import ru.gildor.coroutines.retrofit.Result

interface CurrencyCountryAPIAction {
    suspend fun getCountryByCurrencyName(currencyName: String): Result<List<CurrencyCountryDTO>>
}