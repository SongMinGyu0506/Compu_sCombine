package com.comcombine.backend.service

import com.comcombine.backend.config.except.CrawlingException
import com.comcombine.backend.entity.Computer
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponents
import org.springframework.web.util.UriComponentsBuilder

@Service
class DanawaCrawling:CrawlingService {
    override fun makeURI(keyword: String): String {
        val uriComponents: UriComponents = UriComponentsBuilder.newInstance()
            .scheme("https")
            .host("search.danawa.com")
            .path("dsearch.php")
            .encode(Charsets.UTF_8)
            .queryParam("query",keyword)
            .queryParam("originalQuery",keyword)
            .queryParam("volumeType","all")
            .queryParam("page",1)
            .queryParam("limit",120)
            .queryParam("sort","saveDESC")
            .queryParam("boost","true")
            .build(true);
        return uriComponents.toUriString()
    }

    override fun connection(url: String): Elements {
        val conn: Connection = Jsoup.connect(url)
        val dataDocument: Elements
        try {
            val document: Document = conn.get()
            dataDocument = document.select(".product_list")
        } catch (e: Exception) {
            throw CrawlingException("CrawlingException: " + Throwable().stackTrace[0].methodName.toString())
        }
        return dataDocument
    }

    override fun dataParser(elements: Elements, type:String, keyword:String): List<Computer> {
        val list: ArrayList<Computer> = ArrayList<Computer>()
        elements.select(".prod_main_info").forEach {
                element ->
            list.add(Computer(
                name = element.select(".prod_name").text(),
                comType = type,
                imgUrl = "https:"+element.select(".thumb_image > a > img").attr("data-src"),
                spec = element.select(".spec_list").text(),
                price = element.select("click_log_product_standard_price_ > strong").text(),
                searchTag = keyword,
                originalUrl = element.select(".prod_name > a").attr("href")
            ))
        }
        return list
    }
}