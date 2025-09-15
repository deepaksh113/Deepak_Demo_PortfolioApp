package com.portfolio.app.domain.usecase

import com.portfolio.app.domain.model.Portfolio
import com.portfolio.app.domain.model.Stock
import com.portfolio.app.domain.repository.PortfolioRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GetPortfolioUseCaseTest {

    private lateinit var portfolioRepository: PortfolioRepository
    private lateinit var getPortfolioUseCase: GetPortfolioUseCase

    @Before
    fun setUp() {
        portfolioRepository = mockk()
        getPortfolioUseCase = GetPortfolioUseCase(portfolioRepository)
    }

    @Test
    fun `invoke should return portfolio from repository`() = runTest {
        // Given
        val mockPortfolio = createMockPortfolio()
        coEvery { portfolioRepository.getPortfolio() } returns flowOf(mockPortfolio)

        // When
        val result = getPortfolioUseCase()

        // Then
        val portfolio = result.first()
        assertEquals(mockPortfolio, portfolio)
    }

    @Test
    fun `invoke should propagate repository exceptions`() = runTest {
        // Given
        val exception = RuntimeException("Repository error")
        coEvery { portfolioRepository.getPortfolio() } throws exception

        // When & Then
        try {
            getPortfolioUseCase().first()
            fail("Expected exception to be thrown")
        } catch (e: RuntimeException) {
            assertEquals("Repository error", e.message)
        }
    }

    @Test
    fun `invoke should handle empty portfolio`() = runTest {
        // Given
        val emptyPortfolio = Portfolio(
            currentValue = 0.0,
            totalInvestment = 0.0,
            todaysProfitLoss = 0.0,
            overallProfitLoss = 0.0,
            overallProfitLossPercentage = 0.0,
            stocks = emptyList()
        )
        coEvery { portfolioRepository.getPortfolio() } returns flowOf(emptyPortfolio)

        // When
        val result = getPortfolioUseCase()

        // Then
        val portfolio = result.first()
        assertEquals(emptyPortfolio, portfolio)
        assertTrue(portfolio.stocks.isEmpty())
    }

    private fun createMockPortfolio(): Portfolio {
        return Portfolio(
            currentValue = 75000.0,
            totalInvestment = 70000.0,
            todaysProfitLoss = 1000.0,
            overallProfitLoss = 5000.0,
            overallProfitLossPercentage = 7.14,
            stocks = listOf(
                Stock(
                    symbol = "MAHABANK",
                    name = "MAHABANK",
                    ltp = 38.05,
                    avgPrice = 35.0,
                    netQty = 990,
                    profitLoss = 3019.5,
                    profitLossPercentage = 8.56
                )
            )
        )
    }
}
