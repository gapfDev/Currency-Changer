package com.alxdev.two.moneychanger.util.di

import com.alxdev.two.moneychanger.datax.changer.ChangerLocalDataSourceImp
import com.alxdev.two.moneychanger.datax.changer.ChangerExternalDataSourceImp
import com.alxdev.two.moneychanger.datax.changer.repository.ChangerRepositoryImp
import com.alxdev.two.moneychanger.datax.datasource.contract.CountryAPIAction
import com.alxdev.two.moneychanger.datax.datasource.contract.CurrencyCountryAPIAction
import com.alxdev.two.moneychanger.datax.datasource.contract.CurrencyDAOAction
import com.alxdev.two.moneychanger.datax.datasource.contract.HistoryDAOAction
import com.alxdev.two.moneychanger.domainx.repocontract.ChangerLocalDataSource
import com.alxdev.two.moneychanger.domainx.repocontract.ChangerExternalDataSource
import com.alxdev.two.moneychanger.domainx.repocontract.ChangerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)

object RepoModule {

    @Provides
    fun provideChangerDBWorker(
        historyDAOAction: HistoryDAOAction,
        currencyDAOAction: CurrencyDAOAction,
    ): ChangerLocalDataSource {
        return ChangerLocalDataSourceImp(
            historyDAOAction,
            currencyDAOAction
        )
    }

    @Provides
    fun provideChangerNetworkWorker(
        countryAPIAction: CountryAPIAction,
        currencyCountryAPIAction: CurrencyCountryAPIAction
    ): ChangerExternalDataSource {
        return ChangerExternalDataSourceImp(
            countryAPIAction,
            currencyCountryAPIAction,
        )
    }

    @Provides
    fun provideChangerRepository(
        changerLocalDataSource: ChangerLocalDataSource,
        changerExternalDataSource: ChangerExternalDataSource,
    ): ChangerRepository {
        return ChangerRepositoryImp(
            changerLocalDataSource,
            changerExternalDataSource
        )
    }
}