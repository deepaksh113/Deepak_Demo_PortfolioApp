package com.portfolio.app.data.lib.dto

import com.google.gson.annotations.SerializedName

data class UserHoldingDto(
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("ltp")
    val ltp: Double,
    @SerializedName("avgPrice")
    val avgPrice: Double,
    @SerializedName("close")
    val close: Double
)
