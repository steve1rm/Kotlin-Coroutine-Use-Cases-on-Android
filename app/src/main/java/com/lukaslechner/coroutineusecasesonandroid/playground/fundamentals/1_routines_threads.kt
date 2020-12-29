package com.lukaslechner.coroutineusecasesonandroid.playground.fundamentals

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

fun main() = runBlocking{
    println("Start Program")
    threadRoutine(1, 500)
    threadRoutine(2, 300)
    Thread.sleep(1000)
    println("End Program")

    threadSwitchingCouroutine(3, 300)
}

fun threadRoutine(number: Int, delay: Long) {
    thread {
        println("Routine $number starts work ${Thread.currentThread().id}")
        Thread.sleep(delay)
        println("Routine $number has finished")
    }
}

suspend fun threadSwitchingCouroutine(number: Int, delay: Long) {
    println("Coroutine $number starts work on ${Thread.currentThread().name}")
    delay(delay)
    withContext(Dispatchers.Default) {
        println("Coroutine $number starts work on ${Thread.currentThread().name}")
    }
}
