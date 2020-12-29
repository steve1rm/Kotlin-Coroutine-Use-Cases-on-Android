package com.lukaslechner.coroutineusecasesonandroid.playground.fundamentals

fun main() {
    println("Start Program")
    routine(1, 500)
    routine(2, 300)
}

fun routine(number: Int, delay: Long) {
    println("Routine $number starts work")
    Thread.sleep(delay)
    println("Routine $number has finished")
}
