package com.lukaslechner.coroutineusecasesonandroid.playground.structuredconcurrey

import kotlinx.coroutines.*

fun main() {
    runBlocking {
        val scope = CoroutineScope(Dispatchers.Default)

        scope.coroutineContext[Job]?.invokeOnCompletion { handler: Throwable? ->
            if(handler is CancellationException) {
                println("scope has been cancelled")
            }
        }

        val job1 = scope.launch {
            delay(1000)
            println("Child Coroutine #1 has completed")
        }

        job1.invokeOnCompletion { throwable ->
            if(throwable is CancellationException) {
                println("Coroutine #1 has been cancelled")
            }
        }

        val job2 = scope.launch {
            delay(1000)
            println("Child Coroutine #2 has completed")
        }.invokeOnCompletion(object: CompletionHandler {
            override fun invoke(cause: Throwable?) {
                if(cause is CancellationException) {
                    println("Coroutine #2 has been cancelled")
                }
            }
        })

        delay(200)
        job1.cancelAndJoin()

     //   scope.coroutineContext[Job]?.cancelAndJoin()
    }
}
