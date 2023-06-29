package com.comcombine.backend.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    @Bean
    fun openApi():OpenAPI {
        val info: Info = Info().version("v1.0.0").title("Compu's Combine Backend Docs").description("Compu's Combine Backend Docs")
        return OpenAPI().info(info)
    }
}