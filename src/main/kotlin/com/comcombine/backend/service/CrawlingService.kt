package com.comcombine.backend.service

import com.comcombine.backend.entity.Computer
import org.jsoup.select.Elements

interface CrawlingService {
    fun makeURI(keyword: String): String
    fun connection(url: String): Elements
    fun dataParser(elements: Elements, type: String, keyword:String): List<Computer>
}