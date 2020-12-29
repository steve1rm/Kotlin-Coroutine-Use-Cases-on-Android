package com.lukaslechner.coroutineusecasesonandroid.playground.fundamentals

import kotlinx.coroutines.*

fun main() = runBlocking {
    println("Program Started")

    joinAll(
        async { coroutine(1, 500) },
        async { coroutine(2, 300) },
    )

    println("Program Terminated")
}

suspend fun coroutine(number: Int, delay: Long) {
    println("Routine $number starts work")
    delay(delay)
    println("Routine $number has finished")
}
