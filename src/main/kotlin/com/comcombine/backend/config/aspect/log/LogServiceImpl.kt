package com.comcombine.backend.config.aspect.log

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.reflect.CodeSignature
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import java.lang.reflect.Method
import java.util.*
import java.util.stream.Stream

@Service
class LogServiceImpl:LogService {

    override fun getUrl(method: Method, annotationClass: Class<out Annotation>, baseUrl: String): String {
        val annotation: Annotation = method.getAnnotation(annotationClass)
        val value: Array<String>?
        val httpMethod: String
        try {
            value = (annotation.javaClass.getMethod("value").invoke(annotation) as? Array<*>) as? Array<String>
            httpMethod = (annotationClass.simpleName.replace("Mapping","")).uppercase(Locale.getDefault())
        } catch (e: Exception) {
            return ""
        }
        if (value != null && value.isNotEmpty()) {
            return String.format("%s %s%s",httpMethod,baseUrl, value[0])
        }
        return String.format("%s %s%s",httpMethod,baseUrl, "")
    }

    override fun getRequestUrl(joinPoint: JoinPoint, cls: Class<*>): String {
        val methodSignature: MethodSignature = joinPoint.signature as MethodSignature
        val method:Method = methodSignature.method
        val requestMapping: RequestMapping = cls.getAnnotation(RequestMapping::class.java) ?: return "TEST CODE"
        val baseUrl = requestMapping.value[0]

        return Stream.of(
            GetMapping::class.java,
            PutMapping::class.java,
            PostMapping::class.java,
            PatchMapping::class.java,
            DeleteMapping::class.java,
            RequestMapping::class.java)
            .filter(method::isAnnotationPresent)
            .map { mappingClass -> getUrl(method,mappingClass,baseUrl) }
            .findFirst().orElse(null)

    }

    override fun params(joinPoint: JoinPoint): Map<*, *> {
        val codeSignature: CodeSignature = joinPoint.signature as CodeSignature
        val parameterNames: Array<String> = codeSignature.parameterNames
        val args : List<Any> = joinPoint.args.toList()
        val params: HashMap<String,Any> = HashMap<String,Any>()
        for (i: Int in parameterNames.indices) {
            params[parameterNames[i]] = args[i]
        }
        return params
    }
}