package com.comcombine.backend.service

import com.comcombine.backend.config.except.ComputerInvalidDataException
import com.comcombine.backend.config.except.UserConflictException
import com.comcombine.backend.config.except.UserNotFoundException
import com.comcombine.backend.config.security.TokenProvider
import com.comcombine.backend.dao.RedisDao
import com.comcombine.backend.dto.UserDto
import com.comcombine.backend.entity.Computer
import com.comcombine.backend.entity.User
import com.comcombine.backend.repository.UserRepository
import jakarta.servlet.http.HttpServletResponse
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.HashMap

@Service
class UserServiceImpl(private val userRepository: UserRepository,
                      private val tokenProvider: TokenProvider,
                      private val redisDao: RedisDao): UserService {

    override fun getUser(id: Long): User? {
        val userOptional: Optional<User> = userRepository.findById(id)
        if (userOptional.isPresent) {
            return userOptional.get()
        }
        return null
    }

    override fun makeUser(user: User): User {
        if (userRepository.existsByEmail(user.email)) {
            throw UserConflictException("User already exists")
        }
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

    override fun logout(id: Long, response: HttpServletResponse) {
        redisDao.deleteValue(id.toString())
        response.setHeader("Authorization",null)
        response.setHeader("refreshToken",null)
    }

    override fun saveComputer(id:Long, computers:List<Computer>): User {
        val userOptional: Optional<User> = userRepository.findById(id)
        val user: User
        try {
            user = userOptional.get()
        } catch (ex: Exception) {
            throw UserNotFoundException("User not found")
        }
        computers.forEach { computer ->
            user.computers.add(computer)
        }
        try {
            return userRepository.save(user)
        } catch (ex: DataIntegrityViolationException) {
            throw ComputerInvalidDataException("이미 존재하는 부품 추가")
        }
        
    }
}