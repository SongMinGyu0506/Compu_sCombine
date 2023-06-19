package com.comcombine.backend.service

import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class SchedulerService(private val crawlingService: GlobalCrawlingService) {
    @Scheduled(fixedDelay = 3600000)
    @Async
    fun autoCrawling() {
        crawlingService.crawlingInit()
        crawlingService.danawaSave("SSD","2.5SSD");
        crawlingService.danawaSave("VGA","GTX1650");
        crawlingService.danawaSave("VGA","GTX1660");
        crawlingService.danawaSave("VGA","RTX3060");
        crawlingService.danawaSave("VGA","RTX3070");
        crawlingService.danawaSave("VGA","RTX3080");
        crawlingService.danawaSave("VGA","RTX3090");
        crawlingService.danawaSave("VGA","RTX3080");
        crawlingService.danawaSave("VGA","RX6600");
        crawlingService.danawaSave("VGA","RX6700");
        crawlingService.danawaSave("VGA","RX6800");
        crawlingService.danawaSave("CPU","i3");
        crawlingService.danawaSave("CPU","i5");
        crawlingService.danawaSave("CPU","i7");
        crawlingService.danawaSave("CPU","ryzen3");
        crawlingService.danawaSave("CPU","ryzen5");
        crawlingService.danawaSave("CPU","ryzen7");
        crawlingService.danawaSave("SSD","m.2");
    }
}