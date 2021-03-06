package com.alxdev.two.moneychanger.core.dataimp.external

import com.alxdev.two.moneychanger.data.remote.currency.ContryDTO
import ru.gildor.coroutines.retrofit.Result

interface CountryAPIAction {
    suspend fun getCountryResult(): Result<ContryDTO>
}