package com.lukaslechner.coroutineusecasesonandroid.playground.structuredconcurrey

import kotlinx.coroutines.*
import java.lang.Exception

fun main() {
    val scope = CoroutineScope(Dispatchers.Default)

    scope.launch {

        coroutineScope {
            doSomething()
        }

        launch {
            println("Starting Task #3")
            delay(300)
            println("Completed Task #3")
        }
    }

    Thread.sleep(1000)
}

private suspend fun doSomething() = coroutineScope {
    launch {
        println("Starting Task #1")
        delay(400)
        throw Exception()
        println("Completed Task #1")
    }

    launch {
        println("Starting Task #2")
        delay(200)
        println("Completed Task #2")
    }
}

