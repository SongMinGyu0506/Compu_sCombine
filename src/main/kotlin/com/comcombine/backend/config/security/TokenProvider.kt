package com.comcombine.backend.config.security

import com.comcombine.backend.config.except.JwtCustomException
import com.comcombine.backend.dao.RedisDao
import com.comcombine.backend.entity.User
import io.jsonwebtoken.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

@Component
class TokenProvider(private val redisDao: RedisDao) {

    @Value("\${jwt-key}")
    lateinit var JWT_KEY:String

    private fun checkExpired(token: String):Boolean {
        return try {
            val claims: Jws<Claims> = Jwts.parser().setSigningKey(JWT_KEY).parseClaimsJws(token)
            !claims.body.expiration.before(Date())
        } catch (e: ExpiredJwtException) {
            //e.printStackTrace()
            false
        }
    }
    fun validateAndGetUserId(request:HttpServletRequest,response: HttpServletResponse):Long {
        val accessToken = request.getHeader("Authorization").substring(7)
        val refreshToken = request.getHeader("refreshToken")

        if (checkExpired(accessToken)) {
            return Jwts.parser().setSigningKey(JWT_KEY).parseClaimsJws(accessToken).body.subject.toLong()
        }
        else if (!checkExpired(accessToken) && refreshToken != null) {
            val userId:Long =  Jwts.parser().setSigningKey(JWT_KEY).parseClaimsJws(refreshToken).body.subject.toLong()
            if (checkExpired(refreshToken) && existsRefreshToken(refreshToken,userId)) {
                val newAccessToken = createAccessToken(userId)
                response.setHeader("Authorization", "Bearer $newAccessToken")
                return userId
            }
        }

        lateinit var accessClaims: Jws<Claims>
        lateinit var refreshClaims: Jws<Claims>

        var accessExpired: Boolean = false
        var refreshExpired: Boolean = false

        val jwtParser = Jwts.parser().setSigningKey(JWT_KEY)

        accessClaims = jwtParser.parseClaimsJws(accessToken)
        refreshClaims = jwtParser.parseClaimsJws(refreshToken)

        return accessClaims.body.subject.toLong()
    }

    private fun existsRefreshToken(refreshToken: String,userId:Long): Boolean {
        if(redisDao.getValue(userId.toString()) != null) {
            return true
        }
        return false
    }

    fun create(user:User): String {
        val claims: Claims = Jwts.claims().setSubject(user.id.toString())
        val now: Date = Date()

        val accessToken: String = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date.from(Instant.now().plus(10, ChronoUnit.SECONDS)))
            .signWith(SignatureAlgorithm.HS512,JWT_KEY)
            .setIssuer("COMPU'S COMBINE BACKEND")
            .compact()

        val refreshToken: String = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date.from(Instant.now().plus(1,ChronoUnit.DAYS)))
            .signWith(SignatureAlgorithm.HS512,JWT_KEY)
            .compact()
        redisDao.setValue(user.id.toString(), refreshToken, Duration.ofDays(21))

        return "$accessToken::$refreshToken"
    }

    private fun createAccessToken(id: Long): String {
        val claims: Claims = Jwts.claims().setSubject(id.toString())
        val now: Date = Date()

        return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(Date.from(Instant.now().plus(10, ChronoUnit.SECONDS)))
            .signWith(SignatureAlgorithm.HS512,JWT_KEY).setIssuer("COMPU'S COMBINE BACKEND").compact()
    }

}