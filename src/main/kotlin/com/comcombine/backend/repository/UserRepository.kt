package com.comcombine.backend.repository

import com.comcombine.backend.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User,Long> {
    fun findAllBy() : List<User>
    override fun findById(id: Long) : Optional<User>
    fun findByEmailAndPassword(email: String, password: String) : Optional<User>
    fun findUserById(id:Long):User
}