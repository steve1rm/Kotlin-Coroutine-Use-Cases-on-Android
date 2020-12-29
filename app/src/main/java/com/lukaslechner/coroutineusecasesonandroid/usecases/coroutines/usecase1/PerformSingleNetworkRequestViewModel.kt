package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase1

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class PerformSingleNetworkRequestViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performSingleNetworkRequest() {
        uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val listOfAndroidVersions = mockApi.getRecentAndroidVersions()
                val androidFeature =
                    mockApi.getAndroidVersionFeatures(listOfAndroidVersions.last().apiLevel)
                uiState.value = UiState.Success(listOf(androidFeature.androidVersion))
            } catch (exception: Exception) {
                Timber.e(exception)
                uiState.value = UiState.Error(exception.localizedMessage ?: "Unknown")
            }
        }
    }
}