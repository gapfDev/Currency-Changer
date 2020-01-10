package com.alxdev.two.moneychanger

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

//class ChangerViewModelFactory(
//    private val application: Application,
//    private val currencyDao: CurrencyDao
//) : ViewModelProvider.Factory {
//    @Suppress("unchecked_cast")
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(ChangerViewModel::class.java)) {
//            return ChangerViewModel(application, currencyDao) as T
//        } else {
//            throw IllegalArgumentException("Unknown View Model class")
//        }
//    }
//}
