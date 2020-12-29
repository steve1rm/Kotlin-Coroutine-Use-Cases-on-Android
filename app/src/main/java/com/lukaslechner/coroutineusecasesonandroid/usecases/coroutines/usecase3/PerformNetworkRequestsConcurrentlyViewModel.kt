package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase3

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class PerformNetworkRequestsConcurrentlyViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequestsSequentially() {
        viewModelScope.launch {
            uiState.value = UiState.Loading

            try {
                val result = measureTimeMillis {
                    val listOfAndroidFeatures = mutableListOf<VersionFeatures>()

                    listOfAndroidFeatures.add(mockApi.getAndroidVersionFeatures(27))
                    listOfAndroidFeatures.add(mockApi.getAndroidVersionFeatures(28))
                    listOfAndroidFeatures.add(mockApi.getAndroidVersionFeatures(29))

                    uiState.value = UiState.Success(listOfAndroidFeatures)
                }
            }
            catch(exception: Exception) {
                uiState.value = UiState.Error(exception.localizedMessage ?: "Unknown")
            }
        }
    }

    fun performNetworkRequestsConcurrently() {
        viewModelScope.launch {
            uiState.value = UiState.Loading

            try {
                val listOfAndroidFeatures = mutableListOf<VersionFeatures>()

                val job1 = async {  mockApi.getAndroidVersionFeatures(27) }
                val job2 = async {  mockApi.getAndroidVersionFeatures(28) }
                val job3 = async {  mockApi.getAndroidVersionFeatures(29) }

                listOfAndroidFeatures.add(job1.await())
                listOfAndroidFeatures.add(job2.await())
                listOfAndroidFeatures.add(job3.await())

                uiState.value = UiState.Success(listOfAndroidFeatures)
            }
            catch(exception: Exception) {
                uiState.value = UiState.Error(exception.localizedMessage ?: "Unknown")
            }
        }

    }
}