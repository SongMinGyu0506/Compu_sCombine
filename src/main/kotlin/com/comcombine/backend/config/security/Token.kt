package com.comcombine.backend.config.security

data class Token(val accessToken: String, val refreshToken: String, val key: String)
