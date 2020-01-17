package com.alxdev.two.moneychanger

import android.app.Application
import android.content.Context
import com.alxdev.two.moneychanger.data.remote.Constants
import com.alxdev.two.moneychanger.data.local.MoneyChangerDataBase
import com.alxdev.two.moneychanger.data.remote.RetrofitBuild
import com.alxdev.two.moneychanger.repo.ChangerRepository

class AppApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: AppApplication? = null
        private fun applicationCon(): Context {
            return instance!!.applicationContext
        }

        val currencyListRetrofitBuild: RetrofitBuild
            get() = RetrofitBuild(
                Constants.API.BASE_URL
            )

        val currencyCountryRetrofitBuild: RetrofitBuild
            get() = RetrofitBuild(
                Constants.API.COUNTRY_BASE_URL
            )

        private val moneyChangerDataBase
            get() = MoneyChangerDataBase.getInstance(applicationCon())

        val changerRepository
            get() = ChangerRepository.getInstance(moneyChangerDataBase)
    }
}