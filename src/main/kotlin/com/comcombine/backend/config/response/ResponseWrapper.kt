package com.comcombine.backend.config.response

import org.springframework.http.HttpStatus
import java.time.LocalDate

class ResponseWrapper(val time: LocalDate, val status:HttpStatus, val data:List<Any>) {
    companion object {
        fun addObject(obj:Any, status:HttpStatus): ResponseWrapper {
            val dataResult = ArrayList<Any>()
            dataResult.add(obj)
            return ResponseWrapper(LocalDate.now(), status, dataResult)
        }
    }
}