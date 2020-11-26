package com.alxdev.two.moneychanger.data.remote

import com.alxdev.two.moneychanger.data.remote.currency.CurrencyDTO
import com.alxdev.two.moneychanger.data.remote.currencycountry.CurrencyCountryDTO
import fr.speekha.httpmocker.Mode
import fr.speekha.httpmocker.builder.mockInterceptor
import fr.speekha.httpmocker.model.ResponseDescriptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

class AppRetrofit {
    private val currencyAccessKeyJson: String = Constants.MockData.JSON
    private val countryCurrencyNameJson: String = ""

    private val dynamicMock: (Request) -> ResponseDescriptor = { _request ->

        _request.url.toString().let { _url ->
            when {
                _url.contains("/live", true) -> {
                    ResponseDescriptor(code = 200, body = currencyAccessKeyJson, delay = 2000)
                }
                _url.contains("rest/v2/currency/", true) -> {
                    ResponseDescriptor(code = 200, body = countryCurrencyNameJson, delay = 200)
                }
                else -> {
                    ResponseDescriptor(code = 400, body = "")
                }
            }
        }
    }

    fun getAPI(
        baseURL: String,
        factory: Converter.Factory = GsonConverterFactory.create(),
        client: OkHttpClient
    ): Retrofit {

        return Retrofit.Builder().apply {
            this.baseUrl(baseURL)
            this.client(client)
            this.addConverterFactory(factory)
        }
            .build()
    }

    fun getOkHttpClient(status: Mode? = Mode.DISABLED): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val mockInterceptor = mockInterceptor {
            useDynamicMocks(dynamicMock)
            setInterceptorStatus(status!!)
        }

        return OkHttpClient().newBuilder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(mockInterceptor)
            .build()
    }
}


interface CurrencyAPIService {
    @GET("live")
    suspend fun getCurrency(@Query("access_key") accessKey: String): CurrencyDTO

    @GET("live")
    fun getCurrencyResult(@Query("access_key") accessKey: String): Call<CurrencyDTO>


}

interface CurrencyCountryAPIService {
    @GET("rest/v2/currency/{currencyName}")
    fun getCountryByCurrencyName(@Path("currencyName") currencyName: String): Call<List<CurrencyCountryDTO>>
}
