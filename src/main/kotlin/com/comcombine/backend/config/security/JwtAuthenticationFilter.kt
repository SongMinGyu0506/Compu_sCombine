package com.comcombine.backend.config.security

import com.comcombine.backend.config.except.JwtCustomException
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(private val tokenProvider: TokenProvider):OncePerRequestFilter() {

    fun parseBearerToken(request: HttpServletRequest): String? {
        val bearerToken:String? = request.getHeader("Authorization")
        if (bearerToken != null) {
            if (StringUtils.hasText(bearerToken)&&bearerToken.startsWith("Bearer ")) {
                return bearerToken.substring(7)
            }
        }
        return null
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.getHeader("Authorization") != null) {
            val userId = tokenProvider.validateAndGetUserId(request,response)
            val authentication:AbstractAuthenticationToken = UsernamePasswordAuthenticationToken(userId,null,AuthorityUtils.NO_AUTHORITIES)
            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
            val securityContext: SecurityContext = SecurityContextHolder.createEmptyContext()
            securityContext.authentication = authentication
            SecurityContextHolder.setContext(securityContext)
        }
//        val token: String? = parseBearerToken(request)
//        if (token != null && token != "null") {
//            val userId = tokenProvider.validateAndGetUserId(request)
//            val authentication:AbstractAuthenticationToken = UsernamePasswordAuthenticationToken(userId,null,AuthorityUtils.NO_AUTHORITIES)
//            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
//            val securityContext: SecurityContext = SecurityContextHolder.createEmptyContext()
//            securityContext.authentication = authentication
//            SecurityContextHolder.setContext(securityContext)
//        }
        filterChain.doFilter(request,response)
    }
}