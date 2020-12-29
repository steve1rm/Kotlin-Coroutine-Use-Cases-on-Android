package com.lukaslechner.coroutineusecasesonandroid.playground.builders

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {
    val job = launch(start = CoroutineStart.LAZY) {
            networkRequest()
            println("Result Received")
        }
    delay(200)
    job.start()
    println("End of runBlocking")
}

suspend fun networkRequest(): String {
    delay(500)
    return "Result"
}