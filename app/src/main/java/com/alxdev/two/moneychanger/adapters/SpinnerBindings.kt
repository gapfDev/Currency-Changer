package com.alxdev.two.moneychanger.adapters

import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.alxdev.two.moneychanger.R
import com.alxdev.two.moneychanger.data.local.entity.Currency


@BindingAdapter("currency_values")
fun setCurrencyValues(appCompatSpinner: AppCompatSpinner, currencyList: List<Currency>?) {
    Log.i("alxx", "${currencyList?.size ?: 0}")
    val adapter =
        ArrayAdapter(
            appCompatSpinner.context,
            R.layout.spinner_item,
            currencyList ?: listOf(Currency(value = 1.0, description = "USA"))
        )
    adapter.setDropDownViewResource(R.layout.spinner_item)

    appCompatSpinner.apply {
        this.adapter = adapter
    }
}

@BindingAdapter(value = ["selectedValue", "selectedValueAttrChanged"], requireAll = false)
fun bindSpinnerData(
    appCompatSpinner: AppCompatSpinner, newSelectedValue: Currency?,
    newTextAttrChanged: InverseBindingListener
) {
    appCompatSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            newTextAttrChanged.onChange()
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
    if (newSelectedValue != null) {
        val pos =
            (appCompatSpinner.adapter as ArrayAdapter<Currency?>).getPosition(newSelectedValue)
        appCompatSpinner.setSelection(pos, true)
    }
}

@InverseBindingAdapter(attribute = "selectedValue", event = "selectedValueAttrChanged")
fun getSelectedValue(appCompatSpinner: AppCompatSpinner): Currency? {
    return appCompatSpinner.selectedItem as Currency
}