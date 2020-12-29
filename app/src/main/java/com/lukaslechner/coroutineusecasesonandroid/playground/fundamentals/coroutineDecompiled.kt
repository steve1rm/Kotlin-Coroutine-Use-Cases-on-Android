package com.lukaslechner.coroutineusecasesonandroid.playground.fundamentals

import kotlinx.coroutines.delay

suspend fun coroutineRunner(number: Int, delay: Long) {
    println("Routine $number starts work")
    delay(delay)
    println("Routine $number has finished")
}
