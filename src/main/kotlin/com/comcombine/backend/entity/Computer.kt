package com.comcombine.backend.entity

import jakarta.persistence.*

@Entity
@Table(name = "TB_COMPUTER")
open class Computer(
    open var name: String,
    open var comType: String,
    open var imgUrl: String,
    @Column(length = 2000) open var spec: String,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) open var id: Long = 0L,
    open var price: String,
    open var originalUrl: String,


    ) {

}