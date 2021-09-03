package com.alxdev.two.moneychanger.appcomponents.presentation.changer

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.alxdev.two.moneychanger.R
import com.alxdev.two.moneychanger.domainx.model.Currency
import com.alxdev.two.moneychanger.domainx.model.CurrencyInformation
import com.alxdev.two.moneychanger.domainx.usecasecontract.*
import com.alxdev.two.moneychanger.util.extension.toCurrencyFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangerViewModel @Inject constructor(
    private val currencyListUseCase: CurrencyListUseCase,
    private val historyUseCase: HistoryUseCase,
    private val defaultCurrencyUseCase: DefaultCurrencyUseCase,
    private val syncCountryWithCurrencyUseCase: SyncCountryWithCurrencyUseCase,
    private val saveHistoryUseCase: SaveHistoryUseCase,
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
        get() = currencyListUseCase.execute()

    val historyChange: LiveData<List<com.alxdev.two.moneychanger.domainx.model.History>>
        get() = historyUseCase.execute()

    val localCurrencyList: List<Currency>
        get() = defaultCurrencyUseCase.execute()

    init {
        initMediators()
        viewModelScope.launch { callCountryCurrencyAPI() }
    }

    private fun initMediators() {
        _mediatorSumSpinner.addSource(_foreignSpinner) {
            _mediatorSumSpinner.value = updateTotal()
        }

        _mediatorSumSpinner.addSource(_localEditText) {
            _mediatorSumSpinner.value = updateTotal()
        }
    }

    private suspend fun callCountryCurrencyAPI() {
        syncCountryWithCurrencyUseCase.execute()
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
            saveHistoryUseCase.execute { getCurrencyChangeInformation() }
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
