package com.alxdev.two.moneychanger.domainx.usecasecontract

import androidx.lifecycle.LiveData
import com.alxdev.two.moneychanger.domainx.model.Currency
import com.alxdev.two.moneychanger.domainx.model.CurrencyInformation
import com.alxdev.two.moneychanger.domainx.model.History
import com.alxdev.two.moneychanger.domainx.repocontract.ChangerRepository
import javax.inject.Inject

class CurrencyListUseCase @Inject constructor(
    private val changerRepositoryImp: ChangerRepository
) {
    fun execute(): LiveData<List<Currency>> {
        return changerRepositoryImp.currencyList
    }
}

class HistoryUseCase @Inject constructor(
    private val changerRepositoryImp: ChangerRepository
) {
    fun execute(): LiveData<List<History>> {
        return changerRepositoryImp.historyLiveData
    }
}

class DefaultCurrencyUseCase @Inject constructor(
    private val changerRepositoryImp: ChangerRepository
) {
    fun execute(): List<Currency> {
        return changerRepositoryImp.getDefaultCurrency()
    }
}

class SyncCountryWithCurrencyUseCase @Inject constructor(
    private val changerRepositoryImp: ChangerRepository
) {
    suspend fun execute() {
        changerRepositoryImp.syncDataSource()
    }
}

class SaveHistoryUseCase @Inject constructor(
    private val changerRepositoryImp: ChangerRepository
) {
    suspend fun execute(data: () -> CurrencyInformation) {
        changerRepositoryImp.saveHistory(data())
    }
}

