package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase6

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import timber.log.Timber

class RetryNetworkRequestViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequest() {
        viewModelScope.launch {
            uiState.value = UiState.Loading
            val numberOfRetries = 2

            try {
                retry(numberOfRetries) {
                    loadRecentAndroidVersions()
                }
            }
            catch (exception: Exception) {
                Timber.e(exception.localizedMessage)
                uiState.value = UiState.Error(exception.localizedMessage ?: "Unknown")
            }
        }
    }

    private suspend fun <T> retry(
        numberOfRetries: Int,
        initialDelayMs: Long = 100,
        maxDelayMs: Long = 10000,
        factory: Double = 10.0,
        block: suspend () -> T): T {
        var currentDelay = initialDelayMs

        repeat(numberOfRetries) {
            try {
                return block()
            }
            catch(exception: Exception) {
                Timber.e(exception.localizedMessage)
            }

            delay(currentDelay)
            currentDelay = (currentDelay * factory).toLong().coerceAtMost(maxDelayMs)
        }
        return block()
    }

    private suspend fun loadRecentAndroidVersions() {
        val listOfAndroidVersions = api.getRecentAndroidVersions()
        uiState.value = UiState.Success(listOfAndroidVersions)
    }
}