package com.alxdev.two.moneychanger.di

import com.alxdev.two.moneychanger.core.data.external.CurrencyAPIAction
import com.alxdev.two.moneychanger.core.data.external.CurrencyCountryAPIAction
import com.alxdev.two.moneychanger.core.data.local.CurrencyDAOAction
import com.alxdev.two.moneychanger.core.data.local.HistoryDAOAction
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
        currencyAPIAction: CurrencyAPIAction,
        currencyCountryAPIAction: CurrencyCountryAPIAction,
    ): ChangerRepository {
        return ChangerRepository(
            historyDAOAction,
            currencyDAOAction,
            currencyAPIAction,
            currencyCountryAPIAction,
        )
    }
}