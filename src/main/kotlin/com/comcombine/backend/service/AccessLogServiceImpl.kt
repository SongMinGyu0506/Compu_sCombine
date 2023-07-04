package com.comcombine.backend.service

import com.comcombine.backend.entity.AccessLog
import com.comcombine.backend.repository.AccessLogRepository
import org.springframework.stereotype.Service

@Service
class AccessLogServiceImpl(private val accessLogRepository: AccessLogRepository):AccessLogService {
    override fun save(accessLog: AccessLog): AccessLog {
        return accessLogRepository.save(accessLog)
    }

}