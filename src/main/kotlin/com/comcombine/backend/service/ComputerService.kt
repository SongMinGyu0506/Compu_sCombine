package com.comcombine.backend.service

import com.comcombine.backend.entity.Computer

interface ComputerService {
    fun getComputerList(): List<Computer>
    fun getComputerList(type: String?, name: String?): List<Computer>
    fun getComputerList(id: List<Long>): List<Computer>
    fun getComputerData(id: Long): Computer
    fun getComputerDataInUser(id: Long): ArrayList<Computer>
    fun deleteComputerDataInUser(id: Long, computerIds: ArrayList<Long>)

}