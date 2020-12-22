package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2.callbacks

import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SequentialNetworkRequestsCallbacksViewModel(
    private val mockApi: CallbackMockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun perform2SequentialNetworkRequest() {
        uiState.value = UiState.Loading

        mockApi.getRecentAndroidVersions().enqueue(object: Callback<List<AndroidVersion>> {
            override fun onResponse(call: Call<List<AndroidVersion>>, response: Response<List<AndroidVersion>>) {

                response.body()?.takeIf { it.isNotEmpty() }?.also {
                    val androidVersion: AndroidVersion = it.last()

                    mockApi.getAndroidVersionFeatures(androidVersion.apiLevel).enqueue(object : Callback<VersionFeatures> {
                        override fun onResponse(call: Call<VersionFeatures>, response: Response<VersionFeatures>) {
                            response.body()?.let { versionsFeatures ->
                                uiState.value = UiState.Success(versionsFeatures)
                            }
                        }

                        override fun onFailure(call: Call<VersionFeatures>, t: Throwable) {
                            uiState.value = UiState.Error(t.localizedMessage ?: "")
                        }
                    })
                } ?: run {
                    uiState.value = UiState.Error("Empty list")
                }
            }

            override fun onFailure(call: Call<List<AndroidVersion>>, t: Throwable) {
                uiState.value = UiState.Error(t.localizedMessage ?: "")
            }
        })
    }

    fun perform2SequentialNetworkRequest1() {
        uiState.value = UiState.Loading

        mockApi.getRecentAndroidVersions().enqueue(object: Callback<List<AndroidVersion>> {
            override fun onResponse(call: Call<List<AndroidVersion>>, response: Response<List<AndroidVersion>>) {
                response.body()?.let { listOfAndroidVersions ->
                    val androidVersion: AndroidVersion = listOfAndroidVersions.last()

                    mockApi.getAndroidVersionFeatures(androidVersion.apiLevel).enqueue(object: Callback<VersionFeatures> {
                        override fun onResponse(call: Call<VersionFeatures>, response: Response<VersionFeatures>) {
                            response.body()?.let { versionFeatures ->
                                uiState.value = UiState.Success(versionFeatures)
                            }
                        }

                        override fun onFailure(call: Call<VersionFeatures>, t: Throwable) {
                            uiState.value = UiState.Error(t.localizedMessage ?: "")
                        }
                    })
                } ?: run {
                    uiState.value = UiState.Error("No available android versions")
                }
            }

            override fun onFailure(call: Call<List<AndroidVersion>>, t: Throwable) {
                uiState.value = UiState.Error(t.localizedMessage ?: "")
            }
        })
    }
}