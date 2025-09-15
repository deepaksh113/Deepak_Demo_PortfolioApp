package com.portfolio.app.data.lib

import com.portfolio.app.data.lib.dto.PortfolioResponseDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiService @Inject constructor(
    private val httpClient: HttpClient
) {
    suspend fun getPortfolioData(): PortfolioResponseDto {
        return httpClient.get("https://35dee773a9ec441e9f38d5fc249406ce.api.mockbin.io/") {
            headers {
                append("Content-Type", "application/json")
            }
        }.body()
    }
}
