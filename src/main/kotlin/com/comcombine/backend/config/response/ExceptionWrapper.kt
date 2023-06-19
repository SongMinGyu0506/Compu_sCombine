package com.comcombine.backend.config.response

import org.springframework.http.HttpStatus
import org.springframework.web.context.request.WebRequest
import java.util.Date

data class ExceptionWrapper(val timeStamp: Date, val status:HttpStatus, val error:String, val description:String) {
    companion object{
        fun makeResponse(ex:Exception,request:WebRequest,status:HttpStatus):ExceptionWrapper {
            var errMsg: String = ""
            if (ex.message != null) {
                errMsg = ex.message!!
            }
            return ExceptionWrapper(
                timeStamp = Date(),
                error = errMsg,
                status = status,
                description = request.getDescription(false)
            )
        }
    }
}
