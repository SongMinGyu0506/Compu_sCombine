package com.comcombine.backend.service

import com.comcombine.backend.entity.Computer

interface ComputerService {
    fun getComputerList(): List<Computer>
    fun getComputerList(type: String?, name: String?): List<Computer>
    fun getComputerData(id: Long): Computer
}