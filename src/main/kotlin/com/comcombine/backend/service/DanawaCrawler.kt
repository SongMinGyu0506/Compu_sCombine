@file:JvmName("DanawaCrawlerKt")

package com.comcombine.backend.service

import com.comcombine.backend.entity.Computer
import com.comcombine.backend.repository.ComputerRepository
import com.opencsv.CSVWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileWriter
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

private const val CRAWLING_DATA_CSV_FILE = "src/main/resources/CrawlingCategory.csv"
private const val DATA_REMARK = "//"
private const val DATA_ROW_DIVIDER = "_"
private const val DATA_PRODUCT_DIVIDER = "|"
private const val STR_NAME = "name"
private const val STR_URL = "url"
private const val STR_CRAWLING_PAGE_SIZE = "crawlingPageSize"

@Service
class DanawaCrawler(private val computerRepository: ComputerRepository) {
    private val errorList = mutableListOf<String>()
    private val crawlingCategory = mutableListOf<Map<String, Any>>()

    init {
        File(CRAWLING_DATA_CSV_FILE).bufferedReader().useLines { lines ->
            lines.forEach { line ->
                val crawlingValues = line.split(",").map { it.trim() }
                if (!crawlingValues[0].startsWith(DATA_REMARK)) {
                    crawlingCategory.add(
                        mapOf(
                            STR_NAME to crawlingValues[0],
                            STR_URL to crawlingValues[1],
                            STR_CRAWLING_PAGE_SIZE to crawlingValues[2].toInt()
                        )
                    )
                }
            }
        }
    }

    fun <T> Iterable<T>.forEachParallel(action: suspend (T) -> Unit) = runBlocking {
        val jobs = map { element ->
            async(Dispatchers.Default) {
                action(element)
            }
        }
        jobs.awaitAll()
    }


    fun startCrawling() {
        val chromeOptions = ChromeOptions()
        chromeOptions.addArguments("--headless")
        chromeOptions.addArguments("--window-size=1920,1080")
        chromeOptions.addArguments("--start-maximized")
        chromeOptions.addArguments("--disable-gpu")
        chromeOptions.addArguments("lang=ko=KR")

        crawlingCategory.forEachParallel { categoryValue ->
            crawlingCategory(categoryValue,chromeOptions)
        }
    }

    private fun crawlingCategory(categoryValue: Map<String, Any>, chromeOptions: ChromeOptions) {
        val crawlingName = categoryValue[STR_NAME].toString()
        val crawlingURL = categoryValue[STR_URL].toString()
        val crawlingSize = categoryValue[STR_CRAWLING_PAGE_SIZE] as Int
        println("Crawling Start: $crawlingName")

        val chromeDriver = ChromeDriver(chromeOptions)
        chromeDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10))
        chromeDriver.get(crawlingURL)

        try {
            val wait = WebDriverWait(chromeDriver, Duration.ofSeconds(5))
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("product_list_cover")))

            val crawlingFile = FileWriter("crawl_data/$crawlingName.csv", true)
            val crawlingDataCsvWriter = CSVWriter(crawlingFile)

            crawlingDataCsvWriter.writeNext(arrayOf(getCurrentDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
            for (i in -1 until crawlingSize) {
                when (i) {
                    -1 -> {
                        chromeDriver.findElement(By.xpath("//li[@data-sort-method='NEW']"))?.click()
                    }
                    0 -> {
                        chromeDriver.findElement(By.xpath("//li[@data-sort-method='BEST']"))?.click()
                    }
                    else -> {
                        if (i % 10 == 0) {
                            chromeDriver.findElement(By.xpath("//a[@class='edge_nav nav_next']"))?.click()
                        } else {
                            chromeDriver.findElement(By.xpath("//a[@class='num '][$i]"))?.click()
                        }
                    }
                }

                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("product_list_cover")))

                // Get Product List
                val productListDiv = chromeDriver.findElement(By.xpath("//div[@class='main_prodlist main_prodlist_list']"))
                val products = productListDiv.findElements(By.xpath("//ul[@class='product_list']/li"))

                for (product in products) {
                    if (product.getAttribute("id")?.isEmpty() == true) {
                        continue
                    }

                    // ad
                    if (product.getAttribute("class")?.contains("prod_ad_item") == true) {
                        continue
                    }
                    if (product.getAttribute("id")?.startsWith("ad") == true) {
                        continue
                    }

                    val productId = product.getAttribute("id")?.substring(11)
                    val productName = product.findElement(By.xpath("./div/div[2]/p/a")).text.trim()
                    //val productImg = product.findElement(By.xpath("./div/div[1]/a/img")).getAttribute("src").trim()
                    val productImg = if (
                        product.findElement(By.xpath("./div/div[1]/a/img")).getAttribute("src").trim() ==
                        "https://img.danawa.com/new/noData/img/noImg_160.gif") {
                        "https:"+product.findElement(By.xpath("./div/div[1]/a/img")).getAttribute("data-original").trim()} else {
                        product.findElement(By.xpath("./div/div[1]/a/img")).getAttribute("src").trim()
                    }


                    val productUrl = product.findElement(By.xpath("./div/div[2]/p/a")).getAttribute("href")
                    val productSpec = product.findElement(By.xpath("./div/div[2]/dl/dd")).text.trim()
                    val productPrices = product.findElements(By.xpath("./div/div[3]/ul/li"))
                    var productPriceStr = ""

                    // Check Mall
                    val isMall = product.findElement(By.xpath("./div/div[3]")).getAttribute("class")?.contains("prod_top5") == true

                    if (isMall) {
                        for (productPrice in productPrices) {
                            if (productPrice.getAttribute("class")?.contains("top5_button") == true) {
                                continue
                            }

                            if (productPriceStr.isNotEmpty()) {
                                productPriceStr += DATA_PRODUCT_DIVIDER
                            }

                            val mallName = productPrice.findElement(By.xpath("./a/div[1]")).text.trim().takeUnless { it.isEmpty() }
                                ?: productPrice.findElement(By.xpath("./a/div[1]/span[1]")).text.trim()

                            val price = productPrice.findElement(By.xpath("./a/div[2]/em")).text.trim()

                            productPriceStr += "$mallName$DATA_ROW_DIVIDER$price"
                        }
                    } else {
                        for (productPrice in productPrices) {
                            if (productPriceStr.isNotEmpty()) {
                                productPriceStr += DATA_PRODUCT_DIVIDER
                            }

                            // Default
                            var productType = productPrice.findElement(By.xpath("./div/p")).text.trim()

                            // like Ram/HDD/SSD
                            // HDD: 'WD60EZAZ, 6TB\n25원/1GB_149,000'
                            productType = productType.replace("\n", DATA_ROW_DIVIDER)

                            // Remove rank text
                            // 1위, 2위 ...
                            productType = removeRankText(productType)

                            val price = productPrice.findElement(By.xpath("./p[2]/a/strong")).text.trim()

                            productPriceStr += if (productType.isNotEmpty()) {
                                "$productType$DATA_ROW_DIVIDER$price"
                            } else {
                                price
                            }
                        }
                    }
                    crawlingDataCsvWriter.writeNext(arrayOf(productId, productName, productPriceStr, productImg, productUrl, productSpec))
                    computerRepository.save(
                        Computer(
                            name = productName,
                            comType = crawlingName,
                            imgUrl = if (productImg == "https://img.danawa.com/new/noData/img/noImg_160.gif") {"NULL"} else {productImg},
                            spec = productSpec,
                            price = productPriceStr,
                            originalUrl = productUrl
                        )
                    )
                }
            }
            chromeDriver.close()
        } catch (e: Exception) {
            println("Error - ${categoryValue[STR_NAME]} ->")
            println(e)
            errorList.add(categoryValue[STR_NAME] as String)
        } finally {
            println("Crawling Finish : ${categoryValue[STR_NAME]}")
        }
    }

    private fun removeRankText(productText: String): String {
        if (productText.length < 2) {
            return productText
        }

        val char1 = productText[0]
        val char2 = productText[1]

        return if (char1.isDigit() && (Character.getNumericValue(char1) in 1..9) && char2 == '위') {
            productText.substring(2).trim()
        } else {
            productText
        }
    }

    private fun getCurrentDate(): LocalDateTime {
        return LocalDateTime.now().plus(9, ChronoUnit.HOURS)
    }

}