package com.comcombine.backend.repository

import com.comcombine.backend.entity.Computer
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification

class ComputerSpec {
    companion object {
        fun equalType(type: String): Specification<Computer> {
            return Specification {
                    root: Root<Computer>, _: CriteriaQuery<*>, builder: CriteriaBuilder -> builder.equal(root.get<String>("comType"), type)
            }
        }
        fun likeName(name: String): Specification<Computer> {
            return Specification {
                root, query, criteriaBuilder -> criteriaBuilder.like(root.get("name"), "%$name%")
            }
        }
    }
}