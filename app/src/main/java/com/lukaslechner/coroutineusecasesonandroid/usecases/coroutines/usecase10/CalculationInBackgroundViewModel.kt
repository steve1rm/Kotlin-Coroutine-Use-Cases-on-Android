package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase10

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.math.BigInteger
import kotlin.system.measureTimeMillis

class CalculationInBackgroundViewModel : BaseViewModel<UiState>() {

    fun performCalculation(factorialOf: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                Timber.d("The coroutineContext :$coroutineContext")
                uiState.postValue(UiState.Loading)

                var result: BigInteger
                val measuredCalculateTimeInMs = measureTimeMillis {
                    result = calculateFactorialOf(factorialOf)
                }

                var resultString = ""
                val measuredToStringInMs = measureTimeMillis {
                    resultString = result.toString()
                }

                uiState.postValue(
                    UiState.Success(
                        resultString,
                        measuredCalculateTimeInMs,
                        measuredToStringInMs
                    )
                )
            }
            catch(exception: Exception) {
                Timber.e(exception)
                uiState.postValue(UiState.Error(exception.localizedMessage ?: "Unknown"))
            }
        }
    }

    private suspend fun calculateFactorialOf(number: Int): BigInteger {
        return withContext(Dispatchers.Default) {
            var factorial = BigInteger.ONE

            for (i in 1..number) {
                factorial = factorial.multiply(i.toBigInteger())
            }
            factorial
        }
    }
}