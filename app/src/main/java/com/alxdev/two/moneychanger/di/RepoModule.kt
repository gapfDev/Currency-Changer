package com.alxdev.two.moneychanger.di

import com.alxdev.two.moneychanger.core.dataimp.external.CountryAPIAction
import com.alxdev.two.moneychanger.core.dataimp.external.CurrencyCountryAPIAction
import com.alxdev.two.moneychanger.core.dataimp.local.CurrencyDAOAction
import com.alxdev.two.moneychanger.core.dataimp.local.HistoryDAOAction
import com.alxdev.two.moneychanger.repo.ChangerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)

object RepoModule {
    @Provides
    fun provideChangerRepository(
        historyDAOAction: HistoryDAOAction,
        currencyDAOAction: CurrencyDAOAction,
        countryAPIAction: CountryAPIAction,
        currencyCountryAPIAction: CurrencyCountryAPIAction,
    ): ChangerRepository {
        return ChangerRepository(
            historyDAOAction,
            currencyDAOAction,
            countryAPIAction,
            currencyCountryAPIAction,
        )
    }
}