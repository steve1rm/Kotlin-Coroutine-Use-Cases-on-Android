package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2.rx

import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class SequentialNetworkRequestsRxViewModel(
    private val mockApi: RxMockApi = mockApi()
) : BaseViewModel<UiState>() {

    private val disposable = CompositeDisposable()

    fun perform2SequentialNetworkRequest() {
        mockApi.getRecentAndroidVersions()
            .flatMap { listOfAndroidVersions ->
                val recentAndroidVersion = listOfAndroidVersions.last()

                mockApi.getAndroidVersionFeatures(recentAndroidVersion.apiLevel)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                uiState.value = UiState.Loading
            }
            .subscribeBy(
                onSuccess = {
                    uiState.value = UiState.Success(it) },
                onError = {
                    uiState.value = UiState.Error(it.localizedMessage ?: "") }
            )
            .addTo(disposable)
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}
