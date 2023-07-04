package com.comcombine.backend.service

import com.comcombine.backend.entity.AccessLog

interface AccessLogService {
    fun save(accessLog: AccessLog):AccessLog
}