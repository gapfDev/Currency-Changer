package com.alxdev.two.moneychanger.ui.changer

import android.util.Log
import androidx.lifecycle.*
import com.alxdev.two.moneychanger.AppApplication
import com.alxdev.two.moneychanger.data.local.entity.Currency
import kotlinx.coroutines.launch

class ChangerViewModel : ViewModel() {

    private val changerRepository
        get() = AppApplication.changerRepository

    val usaCurrencyList: List<Currency> get() = listOf(Currency(value = 1.0, description = "USA"))
    val localEditText = MutableLiveData<String>().apply {
        value = "1"
    }

    val foreignSpinnerValueSelected = MutableLiveData<Currency>()
    val currencyList: LiveData<List<Currency>?> = changerRepository.getCurrencyListLive()
    val foreignEditText: LiveData<String>
        get() {
            return Transformations.map(foreignSpinnerValueSelected) {
                String.format("%.2f", it?.value ?: 0.0)
            }
        }

    private val totalMediator = MediatorLiveData<String>()
    private val _totalEditText: MutableLiveData<String>
        get() = totalMediator
    val totalEditText: LiveData<String> = _totalEditText

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage = _errorMessage

    init {
        initTotalMediators()
        initSyncCurrency()
    }

    private fun initTotalMediators() {
        totalMediator.addSource(foreignSpinnerValueSelected) {
            updateTotal()
        }

        totalMediator.addSource(localEditText) {
            updateTotal()
        }
    }

    private fun updateTotal() {
        val foreignQuantity = foreignSpinnerValueSelected.value?.value ?: 0.0
        val localQuantity = localEditText.value.takeUnless {
            it.isNullOrBlank()
        }?.toDouble() ?: 0.0

        _totalEditText.value = String.format("%.2f", localQuantity * foreignQuantity)
    }

    private fun initSyncCurrency() {
        viewModelScope.launch {
            Log.i("alxxt", "class 0 - ${Thread.currentThread().name}")
            changerRepository.syncCurrencyAPI()
        }
    }

    fun onCLickSave(){}
}