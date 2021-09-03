package com.alxdev.two.moneychanger.datax.datasource.contract

import com.alxdev.two.moneychanger.datasourcex.currency.ContryDTO
import ru.gildor.coroutines.retrofit.Result

interface CountryAPIAction {
    suspend fun getCountryResult(): Result<ContryDTO>
}