package com.comcombine.backend.service

import com.comcombine.backend.entity.User
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component

@Component
interface UserService {
    fun getUser(id: Long) : User?
    fun makeUser(user: User) : User
    fun login(email: String, password: String, response:HttpServletResponse) : HashMap<String,Any>
    fun getEmailById(id: Long): String
    fun logout(id:Long, response: HttpServletResponse)
    fun saveComputer(): User
}