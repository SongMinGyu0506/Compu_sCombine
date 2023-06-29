package com.comcombine.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
open class Computer(
    open var name: String,
    open var comType: String,
    open var imgUrl: String,
    @Column(length = 2000) open var spec: String,
    @Id @GeneratedValue open var id: Long = 0,
    open var price: String,
    open var originalUrl: String,


) {

}