package com.alxdev.two.moneychanger.core.data.external

import com.alxdev.two.moneychanger.data.remote.currency.CurrencyDTO
import ru.gildor.coroutines.retrofit.Result

interface CurrencyAPIAction {
    suspend fun getCurrencyResult(): Result<CurrencyDTO>
}