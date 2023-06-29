package com.comcombine.backend.config.except

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(value = HttpStatus.CONFLICT, reason = "USER DATA CONFLICTED")
class UserConflictException(message: String) : RuntimeException(message) {
}