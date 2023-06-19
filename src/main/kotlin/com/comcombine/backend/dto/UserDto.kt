package com.comcombine.backend.dto

import com.comcombine.backend.entity.User

class UserDto {
    data class UserResponseDto(val email: String) {
    companion object {
        fun entityToDto(entity: User): UserResponseDto {
            return UserResponseDto(entity.email)
        }
    }
    }

    data class SignupDto(val email: String, val password: String, val name: String) {
        companion object {
            fun dtoToEntity(dto: SignupDto): User {
                return User(dto.email, dto.password, dto.name)
            }
            fun entityToDto(entity: User): SignupDto {
                return SignupDto(entity.email, "SECRET", entity.name)
            }
        }
    }

    data class LoginDto(val email: String, val password: String)
}