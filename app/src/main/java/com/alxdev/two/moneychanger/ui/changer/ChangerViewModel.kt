package com.alxdev.two.moneychanger.ui.changer

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.alxdev.two.moneychanger.R
import com.alxdev.two.moneychanger.data.model.Currency
import com.alxdev.two.moneychanger.data.model.CurrencyInformation
import com.alxdev.two.moneychanger.repo.ChangerRepository
import com.alxdev.two.moneychanger.util.extension.cleanCurrencyNameMap
import com.alxdev.two.moneychanger.util.extension.toCurrencyEntity
import com.alxdev.two.moneychanger.util.extension.toCurrencyFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangerViewModel @Inject constructor(
    private val changerRepository: ChangerRepository
) : ViewModel() {

    val _localSpinner = MutableLiveData<Currency>()
    val _foreignSpinner = MutableLiveData<Currency>()
    val _localEditText = MutableLiveData<String>().apply {
        value = "1"
    }

    val foreignEditText: LiveData<String> =
        Transformations.map(_foreignSpinner) {
            it.value.toCurrencyFormat()
        }

    private val _errorMessage = MutableLiveData<String>() //TODO(Need to add error message API/DB)
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
        viewModelScope.launch { callCountryAPI() }
    }

    private fun initMediators() {
        _mediatorSumSpinner.addSource(_foreignSpinner) {
            _mediatorSumSpinner.value = updateTotal()
        }

        _mediatorSumSpinner.addSource(_localEditText) {
            _mediatorSumSpinner.value = updateTotal()
        }
    }

    private suspend fun callCountryAPI() {
        changerRepository.callCountriesAPI { _countryDTO ->
            syncCurrencyCountryAPI(_countryDTO.cleanCurrencyNameMap())
        }
    }

    private suspend fun syncCurrencyCountryAPI(countryMap: Map<String, Double>) {

        val currencyList =
            changerRepository.getCurrencyList(countryMap) { _currencyName, _currencyValue ->
                changerRepository.getCurrencyCountryAPI(_currencyName)?.get(0)
                    ?.toCurrencyEntity(_currencyValue)
            }

        changerRepository.saveCurrencyCountryList(currencyList)
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
