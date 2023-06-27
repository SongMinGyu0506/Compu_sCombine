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
//    @ApiOperation(value = "회원가입 컨트롤러",
//        response = UserDto.SignupDto::class,
//        notes = "<h2>해당 컨트롤러를 통해 회원가입을 진행할 수 있습니다.</h2>"
//    )
    fun signUp(@RequestBody signupDto: UserDto.SignupDto):ResponseEntity<*> {
        val user = userService.makeUser(UserDto.SignupDto.dtoToEntity(signupDto))
        return ResponseEntityWrapper.createResponse("/member/{id}", user.id, UserDto.SignupDto.entityToDto(user))
    }

    @PostMapping("")
//    @ApiOperation(
//        value = "로그인 컨트롤러",
//        response = HashMap::class,
//        notes = "<h2>로그인 컨트롤러로 JWT토큰이 생성됩니다.</h2>"
//    )
    fun login(@RequestBody dto: UserDto.LoginDto, response:HttpServletResponse): ResponseEntity<*> {
        val login:HashMap<String,Any> = userService.login(dto.email, dto.password,response)
        return ResponseEntityWrapper.okResponse(login)
    }

    @DeleteMapping("")
//    @ApiOperation(
//        value = "로그아웃 컨트롤러",
//        notes = "<h2>로그아웃을 진행합니다. JWT 토큰이 삭제됩니다.</h2>"
//    )
    fun logout(@AuthenticationPrincipal id:Long,httpServletResponse: HttpServletResponse): ResponseEntity<*> {
        userService.logout(id,httpServletResponse)
        return ResponseEntityWrapper.noResponse()
    }
}