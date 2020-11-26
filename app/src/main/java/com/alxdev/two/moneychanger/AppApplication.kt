package com.alxdev.two.moneychanger

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppApplication : Application() {

//    init {
//        instance = this
//    }

//    companion object {
//        private var instance: AppApplication? = null
//        private fun applicationCon(): Context {
//            return instance!!.applicationContext
//        }

//       val currencyListRetrofitBuild
//            get() = MockRetrofitController.retrofitBuild(
//                Constants.API.BASE_URL
//            )

//        val currencyCountryRetrofitBuild
//            get() = MockRetrofitController.retrofitBuild(
//                Constants.API.COUNTRY_BASE_URL
//            )
////
//        val mockRetrofit: MockRetrofitController
//            get() = MockRetrofitController
//
//
//        private val moneyChangerDataBase
//            get() = MoneyChangerDataBase.getInstance(applicationCon())
//
//        val changerRepository
//            get() = ChangerRepository.getInstance(moneyChangerDataBase)
//    }
}