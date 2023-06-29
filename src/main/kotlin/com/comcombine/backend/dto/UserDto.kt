package com.comcombine.backend.dto

import com.comcombine.backend.entity.User
import io.swagger.v3.oas.annotations.media.Schema

class UserDto {
    data class UserResponseDto(val email: String) {
    companion object {
        fun entityToDto(entity: User): UserResponseDto {
            return UserResponseDto(entity.email)
        }
    }
    }

    @Schema(description = "회원가입 DTO")
    data class SignupDto(
        @Schema(description = "유저 이메일", nullable = false, example = "test@test.com")
        val email: String,
        @Schema(description = "유저 비밀번호", nullable = false, example = "testPassword")
        val password: String,
        @Schema(description = "유저 이름", nullable = false, example = "testName")
        val name: String) {
        companion object {
            fun dtoToEntity(dto: SignupDto): User {
                return User(
                    email = dto.email,
                    password = dto.password,
                    name = dto.name
                )
            }
            fun entityToDto(entity: User): SignupDto {
                return SignupDto(entity.email, "SECRET", entity.name)
            }
        }
    }
    @Schema(description = "로그인 DTO")
    data class LoginDto(
        @Schema(description = "유저 이메일", nullable = false, example = "test@test.com")
        val email: String,
        @Schema(description = "유저 비밀번호", nullable = false, example = "testPassword")
        val password: String
        )
}