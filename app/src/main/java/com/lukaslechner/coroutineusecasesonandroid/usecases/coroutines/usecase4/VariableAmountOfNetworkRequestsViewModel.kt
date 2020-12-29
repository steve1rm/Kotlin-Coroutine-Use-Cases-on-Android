package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase4

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.lang.Exception

class VariableAmountOfNetworkRequestsViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequestsSequentially() {
        uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val recentVersion = mockApi.getRecentAndroidVersions()
                val listOfVersionFeatures = recentVersion.map { androidVersion ->
                    mockApi.getAndroidVersionFeatures(androidVersion.apiLevel)
                }

                uiState.value = UiState.Success(listOfVersionFeatures)
            }
            catch(exception: Exception) {
                uiState.value = UiState.Error(exception.localizedMessage ?: "Unknown")
            }
        }
    }

    fun performNetworkRequestsConcurrently() {
        viewModelScope.launch {
            try {
                uiState.value = UiState.Loading

                val listOfRecentVersion = mockApi.getRecentAndroidVersions()
                val listOfVersionFeatures = listOfRecentVersion.map { androidVersion ->
                    async {
                        mockApi.getAndroidVersionFeatures(androidVersion.apiLevel)
                    }
                }.awaitAll()

                uiState.value = UiState.Success(listOfVersionFeatures)
            }
            catch(exception: Exception) {
                uiState.value = UiState.Error(exception.localizedMessage ?: "Unknown")
            }
        }
    }
}