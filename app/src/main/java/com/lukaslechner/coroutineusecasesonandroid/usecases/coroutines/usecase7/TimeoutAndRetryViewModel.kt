package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase7

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.*
import timber.log.Timber

class TimeoutAndRetryViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequest() {
        uiState.value = UiState.Loading
        val numberOfRetries = 2
        val timeout = 1_000L

        val android27 = viewModelScope.async {
            retryWithTimeout(numberOfRetries, timeout) {
                api.getAndroidVersionFeatures(27)
            }
        }

        val android28 = viewModelScope.async {
            retryWithTimeout(numberOfRetries, timeout) {
                api.getAndroidVersionFeatures(28)
            }
        }

        viewModelScope.launch {
            try {
                val listOfVersionFeatures = awaitAll(android27, android28)
                uiState.value = UiState.Success(listOfVersionFeatures)
            }
            catch(exception: Exception) {
                Timber.e(exception.localizedMessage ?: "Unknown")
                uiState.value = UiState.Error(exception.localizedMessage ?: "Unknown")
            }
        }
    }

    private suspend fun <T> retryWithTimeout(numberOfRetries: Int, timeout: Long, block: suspend () -> T): T {
        return retry(numberOfRetries) {
            withTimeout(timeout) {
                block()
            }
        }
    }

    private suspend fun <T> retry(numberOfRetries: Int, interval: Long = 1_000, block: suspend () -> T): T {
        repeat(numberOfRetries) {
            try {
                return block()
            }
            catch (exception: Exception) {
                Timber.e(exception.localizedMessage)
            }
            delay(interval)
        }
        return block()
    }
}
