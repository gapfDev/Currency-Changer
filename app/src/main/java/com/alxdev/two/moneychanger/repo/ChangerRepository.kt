package com.alxdev.two.moneychanger.repo

import android.util.Log
import com.alxdev.two.moneychanger.*
import com.alxdev.two.moneychanger.data.remote.Constants
import com.alxdev.two.moneychanger.data.local.MoneyChangerDataBase
import com.alxdev.two.moneychanger.data.remote.CurrencyAPIService
import com.alxdev.two.moneychanger.data.local.entity.Currency
import com.alxdev.two.moneychanger.data.remote.CurrencyDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChangerRepository private constructor(private val moneyChangerDataBase: MoneyChangerDataBase) {

    companion object {
        @Volatile
        private var INSTANCE: ChangerRepository? = null

        fun getInstance(moneyChangerDataBase: MoneyChangerDataBase): ChangerRepository {
            synchronized(this) {
                var instance =
                    INSTANCE

                if (instance == null) {
                    instance =
                        ChangerRepository(
                            moneyChangerDataBase
                        )
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    private val client: CurrencyAPIService by lazy {
        AppApplication.retrofitBuild.retrofit.create(
            CurrencyAPIService::class.java
        )
    }

    suspend fun getCurrencyAPI(): CurrencyDTO? {
        return client.getCurrency(Constants.Key.ACCESS_KEY)
    }


    suspend fun saveCurrencyList(currencyList: List<Currency>) {
        withContext(Dispatchers.IO) {
            moneyChangerDataBase.currencyDAO.clear()
           currencyList.map {
               insertCurrency(it)
           }
        }
    }



    private suspend fun insertCurrency(currency: Currency) {
        withContext(Dispatchers.IO) {
            Log.d("alxx", "Element to save > ${currency.value}")
            moneyChangerDataBase.currencyDAO.insert(currency)
        }
    }

    suspend fun getCurrency(): Currency? {
        return withContext(Dispatchers.IO) {
            val data = moneyChangerDataBase.currencyDAO.getAllLite()?.get(0)
            Log.d(
                "alxx",
                "get data <- ${data?.description} ${data?.value} ${moneyChangerDataBase.currencyDAO.getAllLite()?.size}"
            )
            data
        }
    }

    suspend fun getCurrencyList(): List<Currency> {
        return withContext(Dispatchers.IO) {
            moneyChangerDataBase.currencyDAO.getAllLite()
        } ?: emptyList()
    }


}