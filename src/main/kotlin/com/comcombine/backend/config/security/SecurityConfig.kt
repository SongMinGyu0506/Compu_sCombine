package com.comcombine.backend.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.CorsFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint

@Configuration
@EnableWebSecurity
class SecurityConfig(private val objectMapper: ObjectMapper, private val jwtAuthenticationFilter: JwtAuthenticationFilter) {
    @Bean
    fun filterChain(http: HttpSecurity) : SecurityFilterChain {
        http {
            csrf { disable() }
            sessionManagement { SessionCreationPolicy.STATELESS }
            httpBasic { disable() }
            cors { disable() }
            authorizeRequests {
                authorize("/h2-console/**",permitAll)
                // 다른 경로에 대한 규칙 설정
            }
            headers { frameOptions { disable() } }
            exceptionHandling {
                authenticationEntryPoint = AuthenticationEntryPoint {request, response, authException ->
                    val data = HashMap<String,Any>()
                    data["status"] = HttpServletResponse.SC_FORBIDDEN
                    data["message"] = authException.message.toString()

                    response.status = HttpServletResponse.SC_FORBIDDEN
                    response.contentType = MediaType.APPLICATION_JSON.toString()

                    objectMapper.writeValue(response.outputStream,data)
                }
            }
            addFilterAfter<CorsFilter>(jwtAuthenticationFilter)
        }
        return http.build()
    }
}