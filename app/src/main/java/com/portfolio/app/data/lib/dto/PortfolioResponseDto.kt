package com.portfolio.app.data.lib.dto

import com.google.gson.annotations.SerializedName

data class PortfolioResponseDto(
    @SerializedName("data")
    val data: PortfolioDataDto
)

data class PortfolioDataDto(
    @SerializedName("userHolding")
    val userHolding: List<UserHoldingDto>
)
