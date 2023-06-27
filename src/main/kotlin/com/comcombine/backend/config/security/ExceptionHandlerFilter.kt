package com.comcombine.backend.config.security

import com.comcombine.backend.config.except.JwtCustomException
import com.comcombine.backend.config.response.ExceptionWrapper
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.openqa.selenium.devtools.v85.io.IO
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component
class ExceptionHandlerFilter: OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request,response)
        } catch (e: JwtCustomException) {
            setErrorResponse(HttpStatus.UNAUTHORIZED,response,e)
        } catch (e: ExpiredJwtException) {
            setErrorResponse(HttpStatus.UNAUTHORIZED,response,e)
            e.printStackTrace()
        } catch (e: RuntimeException) {
            setErrorResponse(HttpStatus.UNAUTHORIZED,response,e)
        } catch (e: Exception) {
            setErrorResponse(HttpStatus.UNAUTHORIZED,response,e)
        }
    }
    private fun setErrorResponse(status: HttpStatus,response: HttpServletResponse,ex:Throwable) {
        val objectMapper = ObjectMapper()
        response.status = status.value()
        response.contentType = "application/json"
        val errorResponse: ErrorResponse = ErrorResponse(401, ex.message!!)
        try {
            val json: String = objectMapper.writeValueAsString(errorResponse)
            println(json)
            response.writer.write(json)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
    data class ErrorResponse(val code: Int, val message: String)
}