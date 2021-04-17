package com.alxdev.two.moneychanger.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gildor.coroutines.retrofit.Result

object UtilityNetworkWorker {
    suspend fun <T : Any> getResultManagerFromAPIResult(
        networkAction: suspend () -> Result<T>,
    ): ResultManager<T> {
        return when (val result = withContext(Dispatchers.IO) { networkAction() }) {
            is Result.Ok -> {
                ResultManager(
                    RequestState.FINISHED_CORRECT,
                    result.value,
                )
            }
            is Result.Error -> {
                ResultManager(
                    RequestState.SERVER_ERROR,
                    null,
                )
            }
            is Result.Exception -> {
                ResultManager(
                    RequestState.GENERAL_ERROR,
                    null,
                )
            }
            else -> {
                ResultManager(
                    RequestState.DEFAULT,
                    null,
                )
            }
        }
    }

    suspend fun <T : Any> processAPIResult(
        networkAction: suspend () -> Result<T>,
        successAction: (suspend (T) -> Unit)?,
    ) {
        when (val result = withContext(Dispatchers.IO) { networkAction() }) {
            is Result.Ok -> {
                successAction?.let {
                    withContext(Dispatchers.IO) {
                        it(result.value)
                    }
                }
            }
            is Result.Error -> {}
            is Result.Exception -> {}
        }
    }
}

data class ResultManager<T : Any>(
    var requestState: RequestState,
    var result: T? = null
)

enum class RequestState {
    DEFAULT,
    LOADING,
    FINISHED_CORRECT,
    GENERAL_ERROR,
    NETWORK_ERROR,
    SERVER_ERROR,
    EMPTY_LIST,
}