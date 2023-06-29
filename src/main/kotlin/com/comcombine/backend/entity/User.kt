package com.comcombine.backend.entity

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull
import java.util.TreeSet

@Entity
@Table(name = "TB_USER")
open class User(
    @Id @GeneratedValue open var id: Long = 0,
    open var email: String,
    open var password: String,
    open var name: String,

    @OneToMany(cascade = [CascadeType.ALL])
    @NotNull
    @JoinTable(
        name = "TB_USER_COMPUTER",
        joinColumns = [JoinColumn(name="USER_ID")], inverseJoinColumns = [JoinColumn(name="COMPUTER_ID")]
    )
    open var computers: TreeSet<Computer> = TreeSet()
    ) {

}