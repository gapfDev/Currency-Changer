package com.alxdev.two.moneychanger.ui.changer

import android.util.Log
import androidx.lifecycle.*
import com.alxdev.two.moneychanger.AppApplication
import com.alxdev.two.moneychanger.data.local.entity.Currency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChangerViewModel : ViewModel() {

    private val _usaCurrencyList = MutableLiveData<List<Currency>>().apply {
        value = listOf(Currency(value = 1.0, description = "USA"))
    }
    val usaCurrencyList: LiveData<List<Currency>> = _usaCurrencyList

    val localEditText = MutableLiveData<String>()

    private val _currencyList = MutableLiveData<List<Currency>>().apply {
        value = emptyList()
    }
    val currencyList: LiveData<List<Currency>> = _currencyList

    val foreignValueSpinner = MutableLiveData<Currency>()

    val foreignEditText: LiveData<String>
        get() {
            return Transformations.map(foreignValueSpinner) {
                String.format("%.2f", it?.value ?: 0.0)
            }
        }

    private val totalMediatorLiveData = MediatorLiveData<String>()

    private val _totalEditText: MutableLiveData<String>
        get() = totalMediatorLiveData

    val totalEditText: LiveData<String> = _totalEditText

    private val changerRepository
        get() = AppApplication.changerRepository

    init {
        initTotalSubscribers()
        currencyCacheData()
        initSyncCurrency()
    }

    private fun initTotalSubscribers() {
        totalMediatorLiveData.addSource(foreignValueSpinner) {
            updateTotal()
        }

        totalMediatorLiveData.addSource(localEditText) {
            updateTotal()
        }
    }

    private fun updateTotal() {
        val foreignQuantity = foreignValueSpinner.value?.value ?: 0.0
        val localQuantity = localEditText.value.takeUnless {
            it.isNullOrBlank()
        }?.toDouble() ?: 0.0

        _totalEditText.value = String.format("%.2f", localQuantity * foreignQuantity)
    }

    private fun currencyCacheData() {
        viewModelScope.launch {
            _currencyList.value = changerRepository.getCurrencyList()
        }
    }

    private fun initSyncCurrency() {
        viewModelScope.launch {

            changerRepository.getCurrencyAPI()?.quotes?.takeUnless {
                it.isNullOrEmpty()
            }?.let { _map ->
                createCurrencyList(_map).takeUnless {
                    it.isNullOrEmpty()
                }?.let { _currencyListAPI ->
                    checkToSaveCurrency(_currencyListAPI)
                }
            }
        }
    }

    private suspend fun checkToSaveCurrency(currencyListAPI: List<Currency>) =
        withContext(Dispatchers.IO) {

            currencyList.value?.let { _currentList ->
                //                if (_currentList.deepEqualTo(currencyListAPI)) {
                if (compareListEqual(_currentList, currencyListAPI)) {
                    Log.d("alxx", "DO not save data <<>>")
                } else {
                    Log.d("alxx", "save data >>")
                    changerRepository.saveCurrencyList(currencyListAPI)
                    currencyCacheData()
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