package com.comcombine.backend.controller

import com.comcombine.backend.config.except.UserConflictException
import com.comcombine.backend.config.response.ResponseEntityWrapper
import com.comcombine.backend.dto.UserDto
import com.comcombine.backend.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@RequestMapping("/auth")
@RestController
class UserController(private val userService: UserService) {


    @Operation(summary = "회원가입", description = "회원가입 메서드")
    @ApiResponses(value = [
        ApiResponse(responseCode="201", description = "Successful Operation",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = UserDto.SignupDto::class))]),
        ApiResponse(responseCode="409", description = "Conflict User Data",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = Error::class))]),
        ApiResponse(responseCode="500", description = "Internal Server Error",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = Error::class))])
    ])
    @PostMapping("/member")
    fun signUp(@RequestBody signupDto: UserDto.SignupDto):ResponseEntity<*> {
        val user = userService.makeUser(UserDto.SignupDto.dtoToEntity(signupDto))
        return ResponseEntityWrapper.createResponse("/member/{id}", user.id, UserDto.SignupDto.entityToDto(user))
    }

    @PostMapping("")
    @Operation(summary = "로그인", description = "로그인 메서드")
    @ApiResponses(value = [
        ApiResponse(responseCode="200", description = "Successful Operation",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = HashMap::class))]),
        ApiResponse(responseCode="500", description = "Internal Server Error",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = Error::class))])
    ])
    fun login(@RequestBody dto: UserDto.LoginDto, response:HttpServletResponse): ResponseEntity<*> {
        val login:HashMap<String,Any> = userService.login(dto.email, dto.password,response)
        return ResponseEntityWrapper.okResponse(login)
    }

    @DeleteMapping("")
    @Operation(summary = "로그아웃", description = "로그아웃 메서드, JWT 토큰 삭제")
    @ApiResponses(value = [
        ApiResponse(responseCode="204", description = "Successful Operation"),
        ApiResponse(responseCode="500", description = "Internal Server Error",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = Error::class))])
    ])
    fun logout(@AuthenticationPrincipal id:Long,httpServletResponse: HttpServletResponse): ResponseEntity<*> {
        userService.logout(id,httpServletResponse)
        return ResponseEntityWrapper.noResponse()
    }
}