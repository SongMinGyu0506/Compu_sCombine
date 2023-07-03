package com.comcombine.backend.repository

import com.comcombine.backend.entity.Computer
import com.comcombine.backend.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User,Long> {
    fun findAllBy() : List<User>
    override fun findById(id: Long) : Optional<User>
    fun findByEmailAndPassword(email: String, password: String) : Optional<User>
    @Query("select u from User u where u.id = ?1")
    fun findUserById(id:Long):User
    fun existsByEmail(email: String) : Boolean
    @Query("select u.computers from User u where u.id = ?1")
    fun findComputersById(id: Long): ArrayList<Computer>
}