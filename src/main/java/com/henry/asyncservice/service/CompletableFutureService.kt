package com.henry.asyncservice.service

import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import kotlin.math.max
import kotlin.math.min

@Service
class CompletableFutureService {

    @Autowired
    @Qualifier("asyncExecutor")
    private lateinit var executor: Executor

    companion object : KLogging()

    fun runBlocking(action: () -> Unit) = run(listOf(Unit), { action() }, true, null, null)

    fun <T> runBlocking(data: List<T>, action: (List<T>) -> Unit, maxChunkSize: Int? = null, minChunkSize: Int? = null) = run(data, { action(data) }, true, maxChunkSize, minChunkSize)

    fun runNonBlocking(action: () -> Unit) = run(listOf(Unit), { action() }, false, null, null)

    fun <T> runNonBlocking(data: List<T>, action: (List<T>) -> Unit, maxChunkSize: Int? = null, minChunkSize: Int? = null) = run(data, { action(data) }, false, maxChunkSize, minChunkSize)

    private fun <T> run(data: List<T>, action: (List<T>) -> Unit, blocking: Boolean, maxChunkSize: Int?, minChunkSize: Int?) {
        logger.info("[START] Async")

        val numberOfProcessors = Runtime.getRuntime().availableProcessors()
        val basicChunkSize = data.size / (numberOfProcessors * 2)
        val chunkSize = min(maxChunkSize ?: 1000, max(minChunkSize ?: 1, basicChunkSize))

        logger.info("Available Processors = $numberOfProcessors")
        logger.info("ChunkSize = $chunkSize")

        val futures =
            CompletableFuture.runAsync(Runnable {
                try {
                    logger.info("[START] AsyncTask")

                    action(data)

                    logger.info("API_ASYNC_FINISHED")
                    logger.info("[END] AsyncTask")
                } catch (e: Exception) {
                    logger.error("API_ASYNC_FAILED")
                }
            }, executor)

        if (blocking) {
            CompletableFuture.allOf(futures).join()
        }

        logger.info("[END] Async")
    }
}