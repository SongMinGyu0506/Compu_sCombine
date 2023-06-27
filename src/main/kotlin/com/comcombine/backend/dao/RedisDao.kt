package com.comcombine.backend.dao

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class RedisDao(private val redisTemplate: RedisTemplate<String, String>) {
    fun setValue(key: String, value: String, expireDuration: Duration) {
        val values: ValueOperations<String,String> = redisTemplate.opsForValue()
        values.set(key, value,expireDuration)
    }
    fun setValue(key: String, value: String) {
        val values: ValueOperations<String, String> = redisTemplate.opsForValue()
        values.set(key, value)
    }
    fun getValue(key: String): String? {
        val values: ValueOperations<String, String> = redisTemplate.opsForValue()
        return if (values.get(key) != null) {
            values.get(key)!!
        } else {
            null
        }
    }
    fun deleteValue(key: String) {
        redisTemplate.delete(key)
    }

}