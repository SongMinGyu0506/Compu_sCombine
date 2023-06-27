package com.comcombine.backend.config.except

import io.jsonwebtoken.JwtException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "token expired")
class JwtCustomException(message: String): JwtException(message) {
}