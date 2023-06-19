package com.comcombine.backend.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "TB_USER")
open class User(
    open var email: String,
    open var password: String,
    open var name: String
    ) {
    @Id @GeneratedValue open var id: Long = 0
}