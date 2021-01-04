package com.lukaslechner.coroutineusecasesonandroid.playground.structuredconcurrey

import kotlinx.coroutines.*

fun main() {

    runBlocking {
        val scopeJob = Job()
        val scope: CoroutineScope by lazy {
            CoroutineScope(Dispatchers.Default + scopeJob)
        }

        val passJob = Job()
        val coroutineJob = scope.launch(passJob) {
            println("Starting coroutine")
            delay(1000)
        }

        Thread.sleep(100)

        println("passJob and coroutineJob are references to the same job object ${scopeJob === coroutineJob}")
        println("ScopeJob has children ${scopeJob.children.contains(coroutineJob)}")
    }
}