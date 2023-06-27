package com.comcombine.backend.service

import com.comcombine.backend.config.security.TokenProvider
import com.comcombine.backend.dao.RedisDao
import com.comcombine.backend.dto.UserDto
import com.comcombine.backend.entity.User
import com.comcombine.backend.repository.UserRepository
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.HashMap

@Service
class UserServiceImpl(private val userRepository: UserRepository,
                      private val tokenProvider: TokenProvider): UserService {

    override fun getUser(id: Long): User? {
        val userOptional: Optional<User> = userRepository.findById(id)
        if (userOptional.isPresent) {
            return userOptional.get()
        }
        return null
    }

    override fun makeUser(user: User): User {
        userRepository.save(user)
        return user
    }

    override fun login(email: String, password: String,response: HttpServletResponse): HashMap<String,Any> {
        val userOptional = userRepository.findByEmailAndPassword(email, password)
        if (userOptional.isPresent) {
            val user:User = userOptional.get()
            val returnData = HashMap<String,Any>()

            returnData["token"] = tokenProvider.create(user)
            val token = (returnData["token"] as String).split("::")
            val accessToken = token[0]
            val refreshToken = token[1]

            returnData.remove("token")
            returnData["access_token"] = accessToken
            returnData["refresh_token"] = refreshToken

            response.setHeader("Authorization","Bearer $accessToken")
            response.setHeader("refreshToken",refreshToken)
            returnData["user"] = UserDto.SignupDto.entityToDto(user)
            return returnData
        } else {
            throw IllegalStateException("Not Found User")
        }
    }

    override fun getEmailById(id: Long): String {
        return userRepository.findUserById(id).email
    }
}