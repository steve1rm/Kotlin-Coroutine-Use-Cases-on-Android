

package com.lukaslechner.coroutineusecasesonandroid.playground.structuredconcurrey

import kotlinx.coroutines.*

val scope = CoroutineScope(Dispatchers.Default)

fun main() = runBlocking {

    val job = scope.launch {
        println("Coroutine warming up")
        delay(1000)
        println("Coroutine completed")
    }

    job.invokeOnCompletion { throwable: Throwable? ->
        if(throwable is CancellationException) {
            print("Coroutine was cancelled ${throwable.localizedMessage}")
        }
    }

    println("Before delay")
    delay(50)
    onDestroy()
}

fun onDestroy() {
    println("onDestroy")
    scope.cancel()
}