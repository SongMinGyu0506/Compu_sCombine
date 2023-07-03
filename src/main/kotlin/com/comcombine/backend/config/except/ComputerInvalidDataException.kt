package com.comcombine.backend.config.except

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid Computer Data")
class ComputerInvalidDataException(message: String):RuntimeException(message) {
}