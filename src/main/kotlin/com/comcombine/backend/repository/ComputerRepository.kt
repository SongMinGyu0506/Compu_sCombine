package com.comcombine.backend.repository

import com.comcombine.backend.entity.Computer
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ComputerRepository: JpaRepository<Computer,Long>, JpaSpecificationExecutor<Computer> {
    override fun findAll(keyword: Specification<Computer>): List<Computer>
    override fun findById(id: Long): Optional<Computer>
}