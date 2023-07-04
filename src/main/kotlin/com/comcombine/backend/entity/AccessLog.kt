package com.comcombine.backend.entity

import jakarta.persistence.*

@Entity
@Table(name = "TB_ACCESSLOG")
class AccessLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long = 0L,
    open var email: String,
    open var log: String
):DateAudit() {
}