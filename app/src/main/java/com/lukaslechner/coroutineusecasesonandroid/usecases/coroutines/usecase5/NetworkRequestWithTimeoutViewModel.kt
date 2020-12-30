package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase5

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber

class NetworkRequestWithTimeoutViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequest(timeout: Long) {
        // usingWithTimeout(timeout)
        usingWithTimeoutOrNull(timeout)
    }

    private fun usingWithTimeout(timeout: Long) {
        viewModelScope.launch {
            uiState.value = UiState.Loading

            try {
                withTimeoutOrNull(timeout) {
                    val listOfAndroidVersions = api.getRecentAndroidVersions()
                    uiState.value = UiState.Success(listOfAndroidVersions)
                }
            }
            catch (exception: TimeoutCancellationException) {
                Timber.e(exception.localizedMessage)
                uiState.value = UiState.Error(exception.localizedMessage ?: "Unknown")
            }
            catch (exception: Exception) {
                Timber.e(exception.localizedMessage)
                uiState.value = UiState.Error(exception.localizedMessage ?: "Unknown")
            }
        }
    }

    private fun usingWithTimeoutOrNull(timeout: Long) {
        viewModelScope.launch {
            uiState.value = UiState.Loading

            try {
                val listOfAndroidVersions = withTimeoutOrNull(timeout) {
                    api.getRecentAndroidVersions()
                }

                if(listOfAndroidVersions.isNullOrEmpty()) {
                    uiState.value = UiState.Error("Timeout has occurred")
                }
                else {
                    uiState.value = UiState.Success(listOfAndroidVersions)
                }
            }
            catch(exception: Exception) {
                Timber.e(exception.localizedMessage)
                uiState.value = UiState.Error(exception.localizedMessage ?: "Unknown")
            }
        }
    }
}
