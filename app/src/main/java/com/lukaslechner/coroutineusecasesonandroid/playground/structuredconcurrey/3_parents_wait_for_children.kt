package com.lukaslechner.coroutineusecasesonandroid.playground.structuredconcurrey

import kotlinx.coroutines.*

fun main() {
    runBlocking {
        val scope = CoroutineScope(Dispatchers.Default)

        val parentCoroutineJob = scope.launch {
            launch {
                delay(2000)
                println("Child Coroutine #1 has completed")
            }

            launch {
                delay(1000)
                println("Child Coroutine #2 has completed")
            }
        }

        println("parent has children ${parentCoroutineJob.children.count()}")
        parentCoroutineJob.join()
        println("parent has children ${parentCoroutineJob.children.count()}")
    }
}
