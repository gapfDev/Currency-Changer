package com.alxdev.two.moneychanger.data.remote

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


class RetrofitBuild(baseURL: String) {
    val retrofit: Retrofit by lazy {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        Retrofit.Builder().apply {
            baseUrl(baseURL)
            client(client)
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        }.build()
    }
}

interface CurrencyAPIService {
    @GET("live")
    suspend fun getCurrency(@Query("access_key") accessKey: String): CurrencyDTO

    @GET("live")
    fun getCurrencyResult(@Query("access_key") accessKey: String): Call<CurrencyDTO>
}
