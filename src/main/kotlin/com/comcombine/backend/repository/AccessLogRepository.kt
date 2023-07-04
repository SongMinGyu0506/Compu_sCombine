package com.comcombine.backend.repository

import com.comcombine.backend.entity.AccessLog
import org.springframework.data.jpa.repository.JpaRepository

interface AccessLogRepository: JpaRepository<AccessLog,Long> {
}