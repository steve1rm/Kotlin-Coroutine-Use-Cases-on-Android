package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase8

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.launch
import timber.log.Timber

class RoomAndCoroutinesViewModel(
    private val api: MockApi,
    private val database: AndroidVersionDao
) : BaseViewModel<UiState>() {

    fun loadData() {
        viewModelScope.launch {
            try {
                uiState.value = UiState.Loading.LoadFromDb

                val listOfAndroidVersions = database.getAndroidVersions()
                if (listOfAndroidVersions.isEmpty()) {
                    uiState.value = UiState.Error(DataSource.DATABASE, "Database is empty")
                }
                else {
                    uiState.value = UiState.Success(
                        DataSource.DATABASE,
                        listOfAndroidVersions.mapToUiModelList()
                    )
                }

                uiState.value = UiState.Loading.LoadFromNetwork

                val listOfAndroidVersionsFromApi = api.getRecentAndroidVersions()
                listOfAndroidVersionsFromApi.map { androidVersion ->
                    database.insert(androidVersion.mapToEntity())
                }

                uiState.value = UiState.Success(
                    DataSource.NETWORK,
                    listOfAndroidVersionsFromApi
                )
            }
            catch(exception: Exception) {
                Timber.e(exception.localizedMessage)
                uiState.value = UiState.Error(DataSource.DATABASE, exception.localizedMessage ?: "Unknown")
            }
        }
    }

    fun clearDatabase() {
        viewModelScope.launch {
            database.clear()
        }
    }
}

enum class DataSource(val dataSourceName: String) {
    DATABASE("Database"),
    NETWORK("Network")
}