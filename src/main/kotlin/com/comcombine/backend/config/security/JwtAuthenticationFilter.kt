package com.comcombine.backend.config.security

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
        try {
            val token: String? = parseBearerToken(request)
            if (token != null && token != "null") {
                val userId = tokenProvider.validateAndGetUserId(token)
                val authentication:AbstractAuthenticationToken = UsernamePasswordAuthenticationToken(userId,null,AuthorityUtils.NO_AUTHORITIES)
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                val securityContext: SecurityContext = SecurityContextHolder.createEmptyContext()
                securityContext.authentication = authentication
                SecurityContextHolder.setContext(securityContext)

            }
        } catch (e: Exception) {
            logger.error("Could not set user authentication in security context",e)
        }
        filterChain.doFilter(request,response)
    }
}