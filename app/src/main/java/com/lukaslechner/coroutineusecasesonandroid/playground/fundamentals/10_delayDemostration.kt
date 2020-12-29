package com.lukaslechner.coroutineusecasesonandroid.playground.fundamentals

import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    println("Program Started")

    joinAll(
        async { delayDemo(1, 500) },
        async { delayDemo(2, 300) }
    )

    println("Program Terminated")
}

suspend fun delayDemo(number: Int, delay: Long) {
    println("Coroutine $number starts work")
  //  delay(delay)
    Handler(Looper.getMainLooper())
        .postDelayed({
            println("Coroutine $number has finished")
        }, delay)
}

