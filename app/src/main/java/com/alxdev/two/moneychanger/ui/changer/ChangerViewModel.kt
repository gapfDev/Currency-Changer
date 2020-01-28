package com.alxdev.two.moneychanger.ui.changer

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.alxdev.two.moneychanger.AppApplication
import com.alxdev.two.moneychanger.R
import com.alxdev.two.moneychanger.data.local.entity.Currency
import com.alxdev.two.moneychanger.data.local.entity.History
import com.alxdev.two.moneychanger.data.toCurrencyFormat
import kotlinx.coroutines.launch

class ChangerViewModel : ViewModel() {

    private val changerRepository
        get() = AppApplication.changerRepository

    val localSpinnerValueSelected = MutableLiveData<Currency>()
    val localCurrencyList: List<Currency>
        get() = listOf(
            Currency()
        )
    val localEditText = MutableLiveData<String>().apply {
        value = "1"
    }

    val foreignSpinnerValueSelected = MutableLiveData<Currency>()
    val foreignCurrencyList: LiveData<List<Currency>?> = changerRepository.getCurrencyListLive()
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

    val historyChange: LiveData<List<History>?> = changerRepository.getHistoryListLive()

    init {
        initTotalMediators()
        initSyncCurrencyLaunch()
    }

    private fun initTotalMediators() {
        totalMediator.addSource(foreignSpinnerValueSelected) {
            updateTotal()
        }

        totalMediator.addSource(localEditText) {
            updateTotal()
        }
    }

    private fun initSyncCurrencyLaunch() {
        viewModelScope.launch {
            changerRepository.syncCurrencyAPI()
        }
    }

    private fun updateTotal() {
        val foreignQuantity = foreignSpinnerValueSelected.value?.value ?: 0.0
        val localQuantity = localEditText.value.takeUnless {
            it.isNullOrBlank()
        }?.toDouble() ?: 0.0

        _totalEditText.value = (foreignQuantity * localQuantity).toCurrencyFormat()
    }

    private fun getCurrencyChangeInformation(): CurrencyInformationDTO {
        val foreignCurrencyQuantity = foreignSpinnerValueSelected.value?.value ?: 0.0
        val localCurrencyQuantity = localEditText.value.takeUnless {
            it.isNullOrBlank()
        }?.toDouble() ?: 0.0

        val localCountry =
            localSpinnerValueSelected.value?.countryName ?: localCurrencyList[0].countryName
        val foreignCountry =
            foreignSpinnerValueSelected.value?.countryName ?: localCurrencyList[0].countryName

        return CurrencyInformationDTO(
            localCountry,
            foreignCountry,
            localCurrencyQuantity,
            foreignCurrencyQuantity
        )
    }

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
