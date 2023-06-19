package com.comcombine.backend.config.response

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@Component
class ResponseEntityWrapper {
    companion object {
        fun createResponse(uriPath: String, uriId: Long, data:Any ): ResponseEntity<*> {
            val location: URI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path(uriPath)
                .buildAndExpand(uriId)
                .toUri()
            return ResponseEntity.created(location).body(ResponseWrapper.addObject(data, HttpStatus.CREATED))
        }

        fun okResponse(data:Any?): ResponseEntity<*> {
            return if (data != null) {
                ResponseEntity.ok().body(
                    ResponseWrapper.addObject(data, HttpStatus.OK)
                )
            } else {
                ResponseEntity.ok().body(
                    ResponseWrapper.addObject("NOT DATA",HttpStatus.OK)
                )
            }
        }
        fun noResponse(): ResponseEntity<*> {
            return ResponseEntity<Any>(HttpStatus.NO_CONTENT)
        }
    }
}