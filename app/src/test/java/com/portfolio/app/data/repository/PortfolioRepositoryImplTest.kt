package com.portfolio.app.data.repository

import com.portfolio.app.data.lib.ApiService
import com.portfolio.app.data.lib.dto.PortfolioDataDto
import com.portfolio.app.data.lib.dto.PortfolioResponseDto
import com.portfolio.app.data.lib.dto.UserHoldingDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PortfolioRepositoryImplTest {

    private lateinit var apiService: ApiService
    private lateinit var repository: PortfolioRepositoryImpl

    @Before
    fun setUp() {
        apiService = mockk()
        repository = PortfolioRepositoryImpl(apiService)
    }

    @Test
    fun `getPortfolio should return portfolio with stocks`() = runTest {
        // Given
        val mockResponse = createMockPortfolioResponse()
        coEvery { apiService.getPortfolioData() } returns mockResponse

        // When
        val result = repository.getPortfolio().first()

        // Then
        assertNotNull(result)
        assertEquals(2, result.stocks.size)
        assertEquals("MAHABANK", result.stocks[0].symbol)
        assertEquals("ICICI", result.stocks[1].symbol)
    }

    @Test
    fun `getPortfolio should handle empty holdings`() = runTest {
        // Given
        val emptyResponse = PortfolioResponseDto(
            data = PortfolioDataDto(userHolding = emptyList())
        )
        coEvery { apiService.getPortfolioData() } returns emptyResponse

        // When
        val result = repository.getPortfolio().first()

        // Then
        assertNotNull(result)
        assertTrue(result.stocks.isEmpty())
        assertEquals(0.0, result.currentValue, 0.01)
        assertEquals(0.0, result.totalInvestment, 0.01)
    }

    @Test
    fun `getPortfolio should propagate API exceptions`() = runTest {
        // Given
        val exception = RuntimeException("API Error")
        coEvery { apiService.getPortfolioData() } throws exception

        // When & Then
        try {
            repository.getPortfolio().first()
            fail("Expected exception to be thrown")
        } catch (e: RuntimeException) {
            assertEquals("API Error", e.message)
        }
    }

    private fun createMockPortfolioResponse(): PortfolioResponseDto {
        return PortfolioResponseDto(
            data = PortfolioDataDto(
                userHolding = listOf(
                    UserHoldingDto(
                        symbol = "MAHABANK",
                        quantity = 990,
                        ltp = 38.05,
                        avgPrice = 35.0,
                        close = 40.0
                    ),
                    UserHoldingDto(
                        symbol = "ICICI",
                        quantity = 100,
                        ltp = 118.25,
                        avgPrice = 110.0,
                        close = 105.0
                    )
                )
            )
        )
    }
}