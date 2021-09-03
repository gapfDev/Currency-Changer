package com.alxdev.two.moneychanger.util.di

import com.alxdev.two.moneychanger.datax.datasource.contract.CountryAPIAction
import com.alxdev.two.moneychanger.datax.datasource.network.contractimp.CountryAPIActionImpl
import com.alxdev.two.moneychanger.datax.datasource.contract.CurrencyCountryAPIAction
import com.alxdev.two.moneychanger.datax.datasource.network.contractimp.CurrencyCountryAPIActionImpl
import com.alxdev.two.moneychanger.dataold.remote.AppRetrofit
import com.alxdev.two.moneychanger.dataold.remote.Constants
import com.alxdev.two.moneychanger.dataold.remote.CurrencyAPIService
import com.alxdev.two.moneychanger.dataold.remote.CurrencyCountryAPIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object NetworkModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CurrencyAPI

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CurrencyCountryAPI

    @CurrencyAPI
    @Singleton
    @Provides
    fun provideRetrofitCurrencyAPI(@AppModule.CurrencyOkHttpClient okHttpClient: OkHttpClient): CurrencyAPIService {
        return AppRetrofit().getAPI(
            Constants.API.BASE_URL,
            client = okHttpClient
        ).create(CurrencyAPIService::class.java)
    }

    @CurrencyCountryAPI
    @Singleton
    @Provides
    fun provideRetrofitCurrencyCountryAPI(@AppModule.CurrencyCountryOkHttpClient okHttpClient: OkHttpClient): CurrencyCountryAPIService {
        return AppRetrofit().getAPI(
            Constants.API.COUNTRY_BASE_URL,
            client = okHttpClient
        ).create(CurrencyCountryAPIService::class.java)
    }

    @Provides
    fun providesCurrencyAPIAction(
        @CurrencyAPI currencyAPIService: CurrencyAPIService
    ): CountryAPIAction {
        return CountryAPIActionImpl(currencyAPIService)
    }

    @Provides
    fun providesCurrencyCountryAPIAction(
        @CurrencyCountryAPI currencyCountryAPIService: CurrencyCountryAPIService,
    ): CurrencyCountryAPIAction {
        return CurrencyCountryAPIActionImpl(currencyCountryAPIService)
    }

}