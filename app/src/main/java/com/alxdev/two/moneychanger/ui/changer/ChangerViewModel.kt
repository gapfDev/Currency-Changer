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
    val currencyList: LiveData<List<Currency>?> = changerRepository.getCurrencyList2()
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

            changerRepository.getCurrencyAPI()?.quotes?.takeIf {
                it.isNotEmpty()
            }?.let { _map ->
                createCurrencyList(_map).takeUnless {
                    it.isNullOrEmpty()
                }?.let { _currencyListAPI ->
                    changerRepository.insertCurrencyList(_currencyListAPI)
                }
            }
        }
    }

    private fun createCurrencyList(quotes: MutableMap<String, Double>): List<Currency> =
        mutableListOf<Currency>().apply {
            quotes.forEach {
                this.add(
                    Currency(
                        description = it.key,
                        value = it.value
                    )
                )
            }
        }

    fun <T> Collection<T>.isCurrencyEqual(newCurrencyList: Collection<T>): Boolean {

        return false
    }

    //    Work in progress
    infix fun <T> Collection<T>.deepEqualTo(other: Collection<T>): Boolean {
        return this.containsAll(other) && other.containsAll(this)
    }

    //    Work in progress
    private fun <T, Y> compareListEqual(list1: List<T>, list2: List<Y>): Boolean {
        if (list1.size != list2.size)
            return false

        val mergedLists = list1.zip(list2)
        val b = mergedLists.all {
            it.first?.let { _firts ->
                _firts === it.second
            } ?: false
        }
        return b
    }

    fun onCLickSave() {

    }

//    Work in progress

//    private fun doTheConversion() {
//        val localQuantity  = localEditText.value?.toDouble() ?: 0.0
//        val foreignQuantity = foreignValueSpinner.value?.value ?: 0.0
//
//        totalEditText.value = (localQuantity * foreignQuantity).toString()
//    }
}