package com.comcombine.backend.config.aspect.log

import org.aspectj.lang.JoinPoint
import java.lang.reflect.Method
import kotlin.reflect.KClass

interface LogService {
    fun getUrl(method: Method, annotationClass: Class<out Annotation>, baseUrl: String): String
    fun getRequestUrl(joinPoint: JoinPoint, cls: Class<*>) : String
    fun params(joinPoint: JoinPoint): Map<*,*>
}