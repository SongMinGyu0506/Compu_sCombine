package com.comcombine.backend.service

import com.comcombine.backend.repository.ComputerRepository
import jakarta.annotation.PostConstruct
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class SchedulerService(private val danawaCrawler: DanawaCrawler, private val computerRepository: ComputerRepository) {
    @Scheduled(cron = "0 0 0 1 * *")
    @Async
    fun autoCrawling() {
        computerRepository.deleteAll()
        danawaCrawler.startCrawling()
    }

//    @PostConstruct
//    fun initializeCrawling() {
//        computerRepository.deleteAll()
//        danawaCrawler.startCrawling()
//    }
}