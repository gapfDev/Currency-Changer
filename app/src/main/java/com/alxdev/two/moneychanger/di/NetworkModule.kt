package com.alxdev.two.moneychanger.di

import com.alxdev.two.moneychanger.core.data.external.CurrencyAPIAction
import com.alxdev.two.moneychanger.core.data.external.CurrencyAPIActionImpl
import com.alxdev.two.moneychanger.core.data.external.CurrencyCountryAPIAction
import com.alxdev.two.moneychanger.core.data.external.CurrencyCountryAPIActionImpl
import com.alxdev.two.moneychanger.data.remote.AppRetrofit
import com.alxdev.two.moneychanger.data.remote.Constants
import com.alxdev.two.moneychanger.data.remote.CurrencyAPIService
import com.alxdev.two.moneychanger.data.remote.CurrencyCountryAPIService
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

    @AppModule.CurrencyAPI
    @Singleton
    @Provides
    fun provideRetrofitCurrencyAPI(@AppModule.CurrencyOkHttpClient okHttpClient: OkHttpClient): CurrencyAPIService {
        return AppRetrofit().getAPI(
            Constants.API.BASE_URL,
            client = okHttpClient
        ).create(CurrencyAPIService::class.java)
    }

    @AppModule.CurrencyCountryAPI
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
        @AppModule.CurrencyAPI currencyAPIService: CurrencyAPIService
    ): CurrencyAPIAction {
        return CurrencyAPIActionImpl(currencyAPIService)
    }

    @Provides
    fun providesCurrencyCountryAPIAction(
        @AppModule.CurrencyCountryAPI currencyCountryAPIService: CurrencyCountryAPIService,
    ): CurrencyCountryAPIAction {
        return CurrencyCountryAPIActionImpl(currencyCountryAPIService)
    }

}