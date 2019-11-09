package com.henry.asyncservice

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class AsyncServiceApplication

fun main(args: Array<String>) {
    SpringApplication.run(AsyncServiceApplication::class.java, *args)
}