package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase3

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class PerformNetworkRequestsConcurrentlyViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequestsSequentially() {

    }

    fun performNetworkRequestsConcurrently() {
        uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val oreo = async {
                     mockApi.getAndroidVersionFeatures(27)
                }
                val pie = async {
                    mockApi.getAndroidVersionFeatures(28)
                }
                val ten = async {
                    mockApi.getAndroidVersionFeatures(29)
                }

                val listOfVersionFeatures = awaitAll(oreo, pie, ten)

                uiState.value = UiState.Success(listOfVersionFeatures)
            }
            catch(exception: Exception) {
                uiState.value = UiState.Error(exception.localizedMessage ?: "Unknown")
            }
        }
    }
}