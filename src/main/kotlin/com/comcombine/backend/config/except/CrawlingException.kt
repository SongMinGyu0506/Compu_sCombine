package com.comcombine.backend.config.except

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Crawling Error")
class CrawlingException(message: String) : RuntimeException(message) {

}