package com.comcombine.backend.service

import com.comcombine.backend.config.except.ComputerInvalidDataException
import com.comcombine.backend.entity.Computer
import com.comcombine.backend.entity.User
import com.comcombine.backend.repository.ComputerRepository
import com.comcombine.backend.repository.ComputerSpec
import com.comcombine.backend.repository.UserRepository
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service

@Service
class ComputerServiceImpl(private val computerRepository: ComputerRepository, private val userRepository: UserRepository):ComputerService {
    override fun getComputerList(): List<Computer> {
        TODO("Not yet implemented")
    }

    override fun getComputerList(type: String?, name: String?): List<Computer> {
        var spec: Specification<Computer> = Specification{ root, query, criteriaBuilder -> null }
        if (type != null) {
            spec = spec.and(ComputerSpec.equalType(type))
        }
        if (name != null) {
            spec = spec.and(ComputerSpec.likeName(name))
        }
        return computerRepository.findAll(spec)
    }

    override fun getComputerList(id: List<Long>): List<Computer> {
        val list: ArrayList<Computer> = ArrayList()
        id.forEach {
            val computer: Computer
            try {
                computer = computerRepository.findById(it).get()
                list.add(computer)
            } catch (ex: RuntimeException) {
                throw ComputerInvalidDataException("Computer Data not found")
            }
        }
        return list
    }

    override fun getComputerData(id: Long): Computer {
        TODO("Not yet implemented")
    }

    override fun getComputerDataInUser(id: Long): ArrayList<Computer> {
        return userRepository.findComputersById(id)
    }

    override fun deleteComputerDataInUser(id: Long, computerIds: ArrayList<Long>) {
        val user: User = userRepository.findUserById(id)
        val tmp: ArrayList<Computer> = ArrayList(user.computers)

        tmp.removeIf { computer -> computerIds.contains(computer.id) }

        user.computers.clear()
        user.computers.addAll(tmp)
        userRepository.save(user)
    }
}