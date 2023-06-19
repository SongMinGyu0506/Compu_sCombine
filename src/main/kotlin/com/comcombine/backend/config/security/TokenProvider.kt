package com.comcombine.backend.config.security

import com.comcombine.backend.entity.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

@Component
class TokenProvider {

    @Value("\${jwt-key}")
    lateinit var JWT_KEY:String

    fun validateAndGetUserId(token:String):Long {
        val claims: Claims = Jwts.parser().setSigningKey(JWT_KEY).parseClaimsJws(token).body
        return claims.subject.toLong()
    }

    fun create(user:User):String {
        val expireDate: Date = Date.from(Instant.now().plus(1, ChronoUnit.HOURS))
        return Jwts.builder().signWith(SignatureAlgorithm.HS512,JWT_KEY)
            .setSubject(user.id.toString()).setIssuer("COMPU'S COMBINE BACK").setIssuedAt(Date()).setExpiration(expireDate).compact()
    }

//    fun createToken(user:User): Token {
//        val claims: Claims = Jwts.claims().setSubject(user.id.toString())
//        val now: Date = Date()
//
//        val accessToken: String = Jwts.builder()
//            .setClaims(claims)
//            .setIssuedAt(now)
//            .setExpiration(Date.from(Instant.now().plus(10, ChronoUnit.MINUTES)))
//            .signWith(SignatureAlgorithm.HS512,JWT_KEY)
//            .setIssuer("COMPU'S COMBINE BACKEND")
//            .compact()
//
//        val refreshToken: String = Jwts.builder()
//            .setClaims(claims)
//            .setIssuedAt(now)
//            .setExpiration(Date.from(Instant.now().plus(1,ChronoUnit.DAYS)))
//            .signWith(SignatureAlgorithm.HS512,JWT_KEY)
//            .compact()
//
//        return Token(accessToken,refreshToken,user.id.toString())
//    }
//
//    fun validateRefreshToken(refreshToken: )

}