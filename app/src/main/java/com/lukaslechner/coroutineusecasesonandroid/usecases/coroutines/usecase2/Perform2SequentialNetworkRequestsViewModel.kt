package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.launch
import timber.log.Timber

class Perform2SequentialNetworkRequestsViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun perform2SequentialNetworkRequest() {
        viewModelScope.launch {
            try {
                uiState.value = UiState.Loading

                val listOfAndroidVersions = mockApi.getRecentAndroidVersions()
                val androidFeature = mockApi.getAndroidVersionFeatures(listOfAndroidVersions.last().apiLevel)

                uiState.value = UiState.Success(androidFeature)
            }
            catch(exception: Exception) {
                Timber.e(exception.localizedMessage)
                uiState.value = UiState.Error(exception.localizedMessage ?: "")
            }
        }
    }
}