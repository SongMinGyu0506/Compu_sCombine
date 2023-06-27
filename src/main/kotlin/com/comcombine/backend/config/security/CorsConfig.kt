package com.comcombine.backend.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class CorsConfig {

    @Bean
    fun corsFilter():CorsFilter {
        val config:CorsConfiguration = CorsConfiguration()
        val source:UrlBasedCorsConfigurationSource = UrlBasedCorsConfigurationSource()
        config.allowCredentials = true
        config.allowedOrigins = mutableListOf("*")
        config.allowedHeaders = mutableListOf("*")
        config.allowedMethods = mutableListOf("*")
        config.maxAge = 3600L
        config.exposedHeaders = mutableListOf("Authorization")
        config.exposedHeaders = mutableListOf("refreshToken")
        source.registerCorsConfiguration("*",config)
        return CorsFilter(source)
    }
}