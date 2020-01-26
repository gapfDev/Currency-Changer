package com.alxdev.two.moneychanger.ui.changer

import com.alxdev.two.moneychanger.data.local.entity.History
import kotlinx.coroutines.*


class CurrencyInformationDTO(
    val localCountry: String,
    val foreignCountry: String,
    val localCurrencyQuantity: Double,
    val foreignCurrencyQuantity: Double
)

fun CurrencyInformationDTO.toHistory() = History(
    localCountry = localCountry,
    foreignCountry = foreignCountry,
    localCurrencyQuantity = localCurrencyQuantity,
    foreignCurrencyQuantity = foreignCurrencyQuantity
)

class HistoryItem(val itemText: String)


object Coroutines {
    fun io(work: suspend (() -> Unit)): Job =
        CoroutineScope(Dispatchers.IO).launch {
            work()
        }

    fun <T : Any> ioThenMain(work: suspend (() -> T?), callback: ((T?) -> Unit)): Job =
        CoroutineScope(Dispatchers.Main).launch {
            val data = CoroutineScope(Dispatchers.IO).async rt@{
                return@rt work()
            }.await()
            callback(data)
        }
}