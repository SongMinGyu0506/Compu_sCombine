package com.comcombine.backend.dto

import com.comcombine.backend.entity.Computer
import io.swagger.v3.oas.annotations.media.Schema

class ComputerDto {
    @Schema(name = "컴퓨터 부품 리스트 DTO")
    data class ComputerListResponseDto(
        @Schema(description = "부품 이름", nullable = false, example = "testName")
        val name: String,
        @Schema(description = "부품 타입", nullable = false, example = "VGA")
        val comType: String,
        @Schema(description = "부품 이미지 URL", nullable = true)
        val imgUrl: String,
        @Schema(description = "부품 상세설명", nullable = true)
        val spec: String,
        @Schema(description = "부품 가격", nullable = true)
        val price: String,
        @Schema(description = "제품 구입 사이트")
        val originalUrl: String
    ) {
        companion object {
            fun entityToDto(entityList: List<Computer>): List<ComputerListResponseDto> {
                val result: ArrayList<ComputerListResponseDto> = ArrayList()
                entityList.forEach {
                    result.add(
                        ComputerListResponseDto(
                            name = it.name,
                            comType = it.comType,
                            imgUrl = it.imgUrl,
                            spec = it.spec,
                            price = it.price,
                            originalUrl = it.originalUrl
                        )
                    )
                }
                return result
            }
        }
    }
}