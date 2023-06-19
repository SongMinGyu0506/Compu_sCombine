package com.comcombine.backend.service

import com.comcombine.backend.entity.Computer
import com.comcombine.backend.repository.ComputerRepository
import org.jsoup.select.Elements
import org.springframework.stereotype.Service

@Service
class GlobalCrawlingService(private val danawaCrawling: DanawaCrawling, private val computerRepository: ComputerRepository) {
    fun danawaSave(type: String, keyword: String) {
        val connection: Elements = danawaCrawling.connection(danawaCrawling.makeURI(keyword))
        val computers: ArrayList<Computer> = danawaCrawling.dataParser(connection,type,keyword) as ArrayList<Computer>
        computers.forEach { computer ->
            computerRepository.save(computer)
        }
    }
    fun crawlingInit() {
        computerRepository.deleteAllInBatch()
    }
}