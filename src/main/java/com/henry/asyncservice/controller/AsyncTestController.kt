package com.henry.asyncservice.controller

import com.henry.asyncservice.service.AsyncService
import com.henry.asyncservice.service.CompletableFutureService
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/async")
class AsyncTestController {

    companion object : KLogging()

    @Autowired
    private lateinit var completableFutureService: CompletableFutureService

    @Autowired
    private lateinit var asyncService: AsyncService

    @GetMapping("/completable-future/test-1")
    fun cfTest1() {
        for (i in 0..30) {
            completableFutureService.runBlocking {
                logger.info("Completable Future Service Run Blocking Test : $i")
                Thread.sleep(500)
            }
        }

        logger.info("Completable Future Service Run Blocking Complete !!!")
    }

    @GetMapping("/completable-future/test-2")
    fun cfTest2() {
        val dataList = Array(30) { 0 }
        for (i in 0 until dataList.size) {
            dataList[i] = i
        }

        completableFutureService.runBlocking(dataList.toList(), { list ->
            list.forEach { logger.info("Completable Future Service Run Blocking Test : $it") }
        })

        logger.info("Completable Future Service Run Blocking Complete !!!")
    }

    @GetMapping("/completable-future/test-3")
    fun cfTest3() {
        completableFutureService.runNonBlocking {
            for (i in 0..30) {
                logger.info("Completable Future Service Run Non Blocking Test : $i")
            }
        }

        logger.info("Completable Future Service Run Non Blocking !!!!!")
        Thread.sleep(1000)
        logger.info("Completable Future Service Run Non Blocking MAYBE Complete ??????")
    }

    @GetMapping("/completable-future/test-4")
    fun cfTest4() {
        val dataList = Array(100) { 0 }
        for (i in 0 until dataList.size) {
            dataList[i] = i
        }

        completableFutureService.runNonBlocking(dataList.toList(), { list ->
            list.forEach { logger.info("Completable Future Service Run Non Blocking Test : $it") }
        })

        logger.info("Completable Future Service Run Non Blocking !!!!!")
        Thread.sleep(1000)
        logger.info("Completable Future Service Run Non Blocking MAYBE Complete ??????")
    }

    @GetMapping("/async/test-1")
    fun asyncTest1() {
        for (i in 0..100) {
            asyncService.runBlocking {
                logger.info("Async Service Test : $i")
            }
        }

        logger.info("Completable Future Service Run Non Blocking !!!!!")
        Thread.sleep(1000)
        logger.info("Completable Future Service Run Non Blocking MAYBE Complete ??????")
    }
}