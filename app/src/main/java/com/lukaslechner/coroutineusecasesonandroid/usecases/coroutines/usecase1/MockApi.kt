package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase1

import com.google.gson.Gson
import com.lukaslechner.coroutineusecasesonandroid.mock.createMockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.mockAndroidVersions
import com.lukaslechner.coroutineusecasesonandroid.utils.MockNetworkInterceptor

fun mockApi() =
    createMockApi(
        MockNetworkInterceptor()
            .mock(
                "http://localhost/recent-android-versions",
                "Something very bad happened on server side",
                500,
                1500
            )
    )