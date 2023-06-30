package com.comcombine.backend.config.except

import com.comcombine.backend.config.response.ExceptionWrapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class GlobalExceptionalHandler {

    private fun exceptionWrapper(ex: Exception, request:WebRequest, httpStatus: HttpStatus):ResponseEntity<*> {
        ex.printStackTrace()
        return ResponseEntity(ExceptionWrapper.makeResponse(ex,request,httpStatus),httpStatus)
    }
    @ExceptionHandler(Exception::class)
    fun exceptionHandle(ex:Exception, request:WebRequest):ResponseEntity<*> {
        return exceptionWrapper(ex, request, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(CrawlingException::class)
    fun crawlException(ex:Exception, request: WebRequest):ResponseEntity<*> {
        return exceptionWrapper(ex, request, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(JwtCustomException::class)
    fun jwtException(ex:Exception, request: WebRequest):ResponseEntity<*> {
        return exceptionWrapper(ex, request, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(UserConflictException::class)
    fun userConflictException(ex:Exception, request: WebRequest):ResponseEntity<*> {
        return exceptionWrapper(ex, request, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun userNotFoundException(ex:Exception, request: WebRequest):ResponseEntity<*> {
        return exceptionWrapper(ex,request,HttpStatus.BAD_REQUEST)
    }
}