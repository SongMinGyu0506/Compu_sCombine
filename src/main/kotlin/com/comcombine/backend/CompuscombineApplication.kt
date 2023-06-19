package com.comcombine.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class CompuscombineApplication

fun main(args: Array<String>) {
	runApplication<CompuscombineApplication>(*args)
}
