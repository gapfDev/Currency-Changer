package com.alxdev.two.moneychanger.di

import android.content.Context
import com.alxdev.two.moneychanger.core.data.local.CurrencyDAOAction
import com.alxdev.two.moneychanger.core.data.local.CurrencyDAOImp
import com.alxdev.two.moneychanger.core.data.local.HistoryDAOAction
import com.alxdev.two.moneychanger.core.data.local.HistoryDAOImp
import com.alxdev.two.moneychanger.data.local.MoneyChangerDataBase
import com.alxdev.two.moneychanger.data.remote.AppRetrofit
import com.alxdev.two.moneychanger.data.remote.Constants
import com.alxdev.two.moneychanger.data.remote.CurrencyAPIService
import com.alxdev.two.moneychanger.data.remote.CurrencyCountryAPIService
import com.alxdev.two.moneychanger.repo.ChangerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.speekha.httpmocker.Mode
import okhttp3.OkHttpClient
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)

object AppModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CurrencyOkHttpClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CurrencyCountryOkHttpClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CurrencyAPI

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CurrencyCountryAPI

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): MoneyChangerDataBase {
        return MoneyChangerDataBase.getInstance(context)
    }

    @Provides
    fun providesHistoryDAO(moneyChangerDB: MoneyChangerDataBase): HistoryDAOAction = HistoryDAOImp(moneyChangerDB)

    @Provides
    fun providesCurrencyDAO(moneyChangerDB: MoneyChangerDataBase) : CurrencyDAOAction = CurrencyDAOImp(moneyChangerDB)

    @CurrencyAPI
    @Singleton
    @Provides
    fun provideRetrofitCurrencyAPI(@CurrencyOkHttpClient okHttpClient: OkHttpClient): CurrencyAPIService {
        return AppRetrofit().getAPI(
            Constants.API.BASE_URL,
            client = okHttpClient
        ).create(CurrencyAPIService::class.java)
    }

    @CurrencyCountryAPI
    @Singleton
    @Provides
    fun provideRetrofitCurrencyCountryAPI(@CurrencyCountryOkHttpClient okHttpClient: OkHttpClient): CurrencyCountryAPIService {
        return AppRetrofit().getAPI(
            Constants.API.COUNTRY_BASE_URL,
            client = okHttpClient
        ).create(CurrencyCountryAPIService::class.java)
    }

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

    @Provides
    fun provideChangerRepository(
        historyDAOAction: HistoryDAOAction,
        currencyDAOAction: CurrencyDAOAction,
        @CurrencyAPI currencyAPIService: CurrencyAPIService,
        @CurrencyCountryAPI currencyCountryAPIService: CurrencyCountryAPIService,
    ): ChangerRepository {
        return ChangerRepository(
            historyDAOAction,
            currencyDAOAction,
            currencyAPIService,
            currencyCountryAPIService
        )
    }
}