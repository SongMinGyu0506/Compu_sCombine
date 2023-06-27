package com.comcombine.backend.controller

import com.comcombine.backend.config.response.ResponseEntityWrapper
import com.comcombine.backend.dto.UserDto
import com.comcombine.backend.service.UserService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@RequestMapping("/auth")
@RestController
class UserController(private val userService: UserService) {

    @PostMapping("/member")
    fun signUp(@RequestBody signupDto: UserDto.SignupDto):ResponseEntity<*> {
        val user = userService.makeUser(UserDto.SignupDto.dtoToEntity(signupDto))
        return ResponseEntityWrapper.createResponse("/member/{id}", user.id, UserDto.SignupDto.entityToDto(user))
    }

    @PostMapping("")
    fun login(@RequestBody dto: UserDto.LoginDto, response:HttpServletResponse): ResponseEntity<*> {
        val login:HashMap<String,Any> = userService.login(dto.email, dto.password,response)
        return ResponseEntityWrapper.okResponse(login)
    }

    @GetMapping("")
    fun tester(@AuthenticationPrincipal id: Long): ResponseEntity<*> {
        return ResponseEntity.ok().body("tester")
    }
}