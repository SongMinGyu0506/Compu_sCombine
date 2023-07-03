package com.comcombine.backend.controller

import com.comcombine.backend.config.response.ResponseEntityWrapper
import com.comcombine.backend.dto.ComputerDto
import com.comcombine.backend.entity.Computer
import com.comcombine.backend.service.ComputerService
import com.comcombine.backend.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RequestMapping("/computer")
@RestController
class ComputerController(
    private val userService: UserService,
    private val computerService: ComputerService
) {
    @GetMapping("")
    @Operation(summary = "컴퓨터 부품 검색", description = "컴퓨터 부품을 검색합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "검색 성공"),
        ApiResponse(responseCode = "401", description = "로그인 관련 오류"),
        ApiResponse(responseCode = "500", description = "서버 관련 오류")
    ])
    fun findComputerPartsList(@AuthenticationPrincipal id:Long,
                              @RequestParam(value = "type") type: String?,
                              @RequestParam(value = "name") name: String?): ResponseEntity<*> {
        val computerList = computerService.getComputerList(type, name)
        return ResponseEntityWrapper.okResponse(computerList)
    }

    @PostMapping("/member")
    @Operation(summary = "컴퓨터 부품 리스트 계정 등록", description = "사용자의 계정에 컴퓨터 부품을 등록합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "데이터 등록 성공"),
        ApiResponse(responseCode = "400", description = "옳지 않은 계정정보 및 중복데이터 삽입 시도"),
        ApiResponse(responseCode = "500", description = "서버 에러")
    ])
    fun saveComputerPartsInUser(@AuthenticationPrincipal id: Long, @RequestBody computerPartListId: ArrayList<Long>): ResponseEntity<*> {
        val computerList = computerService.getComputerList(computerPartListId)
        userService.saveComputer(id,computerList)
        return ResponseEntityWrapper.createResponse("/user/{id}",id, ComputerDto.ComputerListResponseDto.entityToDto(computerList))
    }

    @GetMapping("/member")
    @Operation(summary = "계정 부품리스트 호출", description = "사용자가 계정에 저장한 부품리스트를 불러옵니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "데이터 호출 성공"),
        ApiResponse(responseCode = "401", description = "계정 관련 에러"),
        ApiResponse(responseCode = "500", description = "서버 내부 에러")
    ])
    fun getComputerPartsInUser(@AuthenticationPrincipal id: Long):ResponseEntity<*> {
        return ResponseEntityWrapper.okResponse(computerService.getComputerDataInUser(id))
    }

    @DeleteMapping("/member")
    @Operation(summary = "계정 등록 부품데이터 삭제", description = "계정에 등록한 부품 데이터를 ID 값으로 삭제합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "데이터 삭제 성공"),
        ApiResponse(responseCode = "401", description = "계정 관련 에러"),
        ApiResponse(responseCode = "500", description = "서버 내부 에러")
    ])
    fun deleteComputerPartsInUser(@AuthenticationPrincipal id: Long, @RequestParam(name = "id") computerId: ArrayList<Long>): ResponseEntity<*> {
        computerService.deleteComputerDataInUser(id,computerId)
        return ResponseEntityWrapper.noResponse()
    }
}