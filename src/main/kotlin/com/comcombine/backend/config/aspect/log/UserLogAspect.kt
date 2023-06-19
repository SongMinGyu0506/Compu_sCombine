package com.comcombine.backend.config.aspect.log

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
class UserLogAspect(private val logService: LogService, private val userService: UserService) {
    private val log = LoggerFactory.getLogger(javaClass)
    @Pointcut("execution(* com.comcombine.backend.controller.*.*(..,!int))")
    fun noAuthUserRequest(){}
    @Pointcut("execution(* com.comcombine.backend.controller.*.*(int,..))")
    fun authUserRequest(){}

    @Around("noAuthUserRequest()")
    fun beforeNoAuthUserLog(joinPoint: ProceedingJoinPoint) : Any {
        val cls : Class<*> = joinPoint.target.javaClass
        val result: Any
        try {
            result = joinPoint.proceed(joinPoint.args)
            return result
        } finally {
            log.info("ENTER "+logService.getRequestUrl(joinPoint,cls)+"\tParams: "+logService.params(joinPoint))
        }
    }

    @Around("authUserRequest()")
    fun beforeAuthUserLog(joinPoint:ProceedingJoinPoint):Any {
        val cls: Class<*> = joinPoint.target.javaClass
        val result: Any
        val targetParameter: Map<*,*> = logService.params(joinPoint)
        val email: String = userService.getEmailById(targetParameter["id"] as Long)
        try {
            result = joinPoint.proceed(joinPoint.args)
            return result
        } finally {
            log.info("ENTER Member: $email"+"\t"+logService.getRequestUrl(joinPoint,cls)+"\tParams: "+logService.params(joinPoint))
        }
    }

}