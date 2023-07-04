package com.comcombine.backend.entity

import jakarta.persistence.*

@Entity
@Table(name = "TB_USER")
open class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) open var id: Long = 0L,
    open var email: String,
    open var password: String,
    open var name: String,

    @OneToMany(cascade = [CascadeType.ALL])
    open var computers: MutableList<Computer> = mutableListOf()
    ):DateAudit() {
}