package com.alxdev.two.moneychanger.datax.changer

import com.alxdev.two.moneychanger.datasourcex.currency.ContryDTO
import com.alxdev.two.moneychanger.datasourcex.currencycountry.CurrencyCountryDTO
import com.alxdev.two.moneychanger.datax.datasource.contract.CountryAPIAction
import com.alxdev.two.moneychanger.datax.datasource.contract.CurrencyCountryAPIAction
import com.alxdev.two.moneychanger.domain.ResultManager
import com.alxdev.two.moneychanger.domain.UtilityNetworkWorker
import com.alxdev.two.moneychanger.domainx.repocontract.ChangerExternalDataSource
import ru.gildor.coroutines.retrofit.Result
import javax.inject.Inject

class ChangerExternalDataSourceImp @Inject constructor(
    private val countryAPIActionImp: CountryAPIAction,
    private val currencyCountryAPIActionImpl: CurrencyCountryAPIAction,
) : ChangerExternalDataSource {
    override suspend fun callCountriesAPI(): ResultManager<ContryDTO> {
        return UtilityNetworkWorker.getResultManagerFromAPIResult {
            countryAPIActionImp.getCountryResult()
        }
    }

    override suspend fun callCountryResult(): Result<ContryDTO> {
        return countryAPIActionImp.getCountryResult()
    }

    override suspend fun getCurrencyCountryAPI(currencyName: String): ResultManager<List<CurrencyCountryDTO>> =
        UtilityNetworkWorker.getResultManagerFromAPIResult {
            currencyCountryAPIActionImpl.getCountryByCurrencyName(currencyName)
        }

    override suspend fun getCountryByCurrencyName(currencyName: String): Result<List<CurrencyCountryDTO>> {
        return currencyCountryAPIActionImpl.getCountryByCurrencyName(currencyName)
    }
}