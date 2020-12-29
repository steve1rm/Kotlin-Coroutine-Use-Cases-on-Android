package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2.callbacks

import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SequentialNetworkRequestsCallbacksViewModel(
    private val mockApi: CallbackMockApi = mockApi()
) : BaseViewModel<UiState>() {


    fun perform2SequentialNetworkRequest() {
        uiState.value = UiState.Loading

        mockApi.getRecentAndroidVersions().enqueue(object : Callback<List<AndroidVersion>> {
            override fun onResponse(call: Call<List<AndroidVersion>>, response: Response<List<AndroidVersion>>) {
                val listOfAndroidVersions: List<AndroidVersion> = response.body() ?: emptyList()

                response.body()?.let { listOfAndroidVersions

                } ?: run {
                    uiState.value = UiState.Error("Empty list")
                }

                val latestAndroidVersion = listOfAndroidVersions.last()

                response.body()?.takeIf { it.isNullOrEmpty() }
                    ?.run {

                    }

            }

            override fun onFailure(call: Call<List<AndroidVersion>>, t: Throwable) {
                uiState.value = UiState.Error(t.localizedMessage)
            }
        })
    }
}