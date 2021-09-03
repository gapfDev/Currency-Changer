package com.alxdev.two.moneychanger.util.di

import android.content.Context
import com.alxdev.two.moneychanger.datax.datasource.contract.CurrencyDAOAction
import com.alxdev.two.moneychanger.datax.datasource.database.contractimp.CurrencyDAOImp
import com.alxdev.two.moneychanger.datax.datasource.contract.HistoryDAOAction
import com.alxdev.two.moneychanger.datax.datasource.database.contractimp.HistoryDAOImp
import com.alxdev.two.moneychanger.datax.datasource.database.MoneyChangerDataBase
import com.alxdev.two.moneychanger.dataold.remote.AppRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.speekha.httpmocker.Mode
import okhttp3.OkHttpClient
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object AppModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CurrencyOkHttpClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CurrencyCountryOkHttpClient

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): MoneyChangerDataBase {
        return MoneyChangerDataBase.getInstance(context)
    }

    @Provides
    fun providesHistoryDAO(moneyChangerDB: MoneyChangerDataBase): HistoryDAOAction =
        HistoryDAOImp(moneyChangerDB)

    @Provides
    fun providesCurrencyDAO(moneyChangerDB: MoneyChangerDataBase): CurrencyDAOAction =
        CurrencyDAOImp(moneyChangerDB)

    @CurrencyOkHttpClient
    @Singleton
    @Provides
    fun provideCurrencyOkHttpClient(): OkHttpClient {
        return AppRetrofit().getOkHttpClient(Mode.ENABLED)
    }

    @CurrencyCountryOkHttpClient
    @Singleton
    @Provides
    fun provideCurrencyCountryOkHttpClient(): OkHttpClient {
        return AppRetrofit().getOkHttpClient()
    }
}