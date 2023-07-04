package com.comcombine.backend.config.aspect.log

import com.comcombine.backend.entity.AccessLog
import com.comcombine.backend.service.AccessLogService
import com.comcombine.backend.service.UserService
import lombok.extern.slf4j.Slf4j
import org.apache.logging.slf4j.SLF4JLogger
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
@Aspect
@Slf4j
class UserLogAspect(private val logService: LogService, private val userService: UserService, private val accessLogService: AccessLogService) {
    private val log = LoggerFactory.getLogger(javaClass)
    @Pointcut("execution(* com.comcombine.backend.controller.*.*(..,!Long))")
    fun noAuthUserRequest(){}
    @Pointcut("execution(* com.comcombine.backend.controller.*.*(Long,..))")
    fun authUserRequest(){}

    @Around("noAuthUserRequest() && !@annotation(com.comcombine.backend.config.annotation.AuthPoint)")
    fun beforeNoAuthUserLog(joinPoint: ProceedingJoinPoint) : Any {
        val cls : Class<*> = joinPoint.target.javaClass
        val result: Any
        try {
            result = joinPoint.proceed(joinPoint.args)
            return result
        } finally {
            accessLogService.save(AccessLog(
                email = "NON_USER",
                log = "ENTER "+logService.getRequestUrl(joinPoint,cls)+"\tParams: "+logService.params(joinPoint)
            ))
            log.info("ENTER "+logService.getRequestUrl(joinPoint,cls)+"\tParams: "+logService.params(joinPoint))
        }
    }

    @Around("@annotation(com.comcombine.backend.config.annotation.AuthPoint)")
    fun beforeAuthUserLog(joinPoint:ProceedingJoinPoint):Any {
        val cls: Class<*> = joinPoint.target.javaClass
        val result: Any
        val targetParameter: Map<*,*> = logService.params(joinPoint)
        val email: String = userService.getEmailById(targetParameter["id"] as Long)
        try {
            result = joinPoint.proceed(joinPoint.args)
            return result
        } finally {
            accessLogService.save(AccessLog(
                email = email,
                log = "ENTER Member: $email"+"\t"+logService.getRequestUrl(joinPoint,cls)+"\tParams: "+logService.params(joinPoint)
            ))
            log.info("ENTER Member: $email"+"\t"+logService.getRequestUrl(joinPoint,cls)+"\tParams: "+logService.params(joinPoint))
        }
    }

}