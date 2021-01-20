package com.alxdev.two.moneychanger.ui.changer

import android.util.Log
import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.alxdev.two.moneychanger.R
import com.alxdev.two.moneychanger.data.model.Currency
import com.alxdev.two.moneychanger.data.model.CurrencyInformation
import com.alxdev.two.moneychanger.repo.ChangerRepository
import com.alxdev.two.moneychanger.util.extension.toCurrencyFormat
import kotlinx.coroutines.launch

class ChangerViewModel @ViewModelInject constructor(
    private val changerRepository: ChangerRepository
) : ViewModel() {

    val _localSpinner = MutableLiveData<Currency>()
    val _foreignSpinner = MutableLiveData<Currency>()
    val _localEditText = MutableLiveData<String>().apply {
        value = "1"
    }

    val foreignEditText: LiveData<String>
        get() {
            return Transformations.map(_foreignSpinner) {
                String.format("%.2f", it?.value ?: 0.0)
            }
        }

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _mediatorSumSpinner = MediatorLiveData<String>()
    val totalEditText: LiveData<String> = _mediatorSumSpinner

    val foreignCurrencyList: LiveData<List<Currency>>
        get() = changerRepository.currencyList

    val historyChange: LiveData<List<com.alxdev.two.moneychanger.data.model.History>>
        get() = changerRepository.historyLiveData

    val localCurrencyList: List<Currency>
        get() = changerRepository.getDefaultCurrency()

    init {
        initMediators()
        viewModelScope.launch { syncCurrencyLaunch() }
    }

    private fun initMediators() {
        _mediatorSumSpinner.addSource(_foreignSpinner) {
            _mediatorSumSpinner.value = updateTotal()
        }

        _mediatorSumSpinner.addSource(_localEditText) {
            _mediatorSumSpinner.value = updateTotal()
        }
    }

    private suspend fun syncCurrencyLaunch() {
        changerRepository.callCurrencyAPI { _currencyDTO ->
            changerRepository.syncCurrencyCountryAPI(
                _currencyDTO
            )
        }
    }

    private fun updateTotal(): String {
        return (getLocalQuantity() * getForeignQuantity()).toCurrencyFormat()
    }

    private fun getCurrencyChangeInformation(): CurrencyInformation {
        val localCountry =
            _localSpinner.value?.countryName ?: localCurrencyList[0].countryName
        val foreignCountry =
            _foreignSpinner.value?.countryName ?: localCurrencyList[0].countryName

        return CurrencyInformation(
            localCountry,
            foreignCountry,
            getLocalQuantity(),
            getForeignQuantity()
        )
    }

    private fun getForeignQuantity(): Double = _foreignSpinner.value?.value ?: 0.0
    private fun getLocalQuantity(): Double = _localEditText.value.takeUnless {
        it.isNullOrBlank()
    }?.toDouble() ?: 0.0

    fun onCLickSaveLaunch() {
        viewModelScope.launch {
            changerRepository.saveHistory(getCurrencyChangeInformation())
        }
    }

    fun onHistoryItemCLick(view: View, valor: String) {
        if (view.id == R.id.data_up) {
            Log.i("alxxC", "UP -- $valor")
        } else if (view.id == R.id.imgDetailHistory) {
            Log.i("alxxC", "Detail -- $valor")
        }
    }
}
