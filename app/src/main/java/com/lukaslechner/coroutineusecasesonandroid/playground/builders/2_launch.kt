package com.lukaslechner.coroutineusecasesonandroid.playground.builders

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() = runBlocking<Unit> {

    var result1 = ""
    val measureTimeMs = measureTimeMillis {
        launch {
            result1 = networkCall(1)
        }
    }
    println("Result received: $result1 after $measureTimeMs")


    launch {
        val result2 = networkCall(2)

    }
}

suspend fun networkCall(number: Int): String {
    delay(500)

    return "Result $number"
}

fun elapsedMillis(startTime: Long) = System.currentTimeMillis() - startTime