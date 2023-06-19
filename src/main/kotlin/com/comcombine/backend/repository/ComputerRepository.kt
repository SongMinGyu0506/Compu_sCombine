package com.comcombine.backend.repository

import com.comcombine.backend.entity.Computer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ComputerRepository: JpaRepository<Computer,Long> {

}