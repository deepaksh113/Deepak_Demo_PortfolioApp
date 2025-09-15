package com.portfolio.app.presentation.viewmodel

import com.portfolio.app.domain.model.Portfolio
import com.portfolio.app.domain.model.Stock
import com.portfolio.app.domain.usecase.GetPortfolioUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PortfolioViewModelTest {

    private lateinit var getPortfolioUseCase: GetPortfolioUseCase
    private lateinit var viewModel: PortfolioViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getPortfolioUseCase = mockk()
        viewModel = PortfolioViewModel(getPortfolioUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should have proper initial state`() {
        // Given & When
        val uiState = viewModel.uiState.value

        // Then
        assertNotNull(uiState)
        assertFalse("Should not be loading", uiState.isLoading)
        assertNull("Error should be null", uiState.error)
    }

//    @Test
//    fun `should handle successful portfolio loading`() = runTest {
//        // Given
//        val mockPortfolio = createMockPortfolio()
//        coEvery { getPortfolioUseCase() } returns flowOf(mockPortfolio)
//
//        // When
//        advanceUntilIdle()
//
//        // Then
//        val uiState = viewModel.uiState.value
//        assertFalse("Should not be loading", uiState.isLoading)
//        assertNotNull("Portfolio should not be null", uiState.portfolio)
//        assertNull("Error should be null", uiState.error)
//    }

    @Test
    fun `should handle error when use case throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { getPortfolioUseCase() } throws exception

        // When
        advanceUntilIdle()

        // Then
        val uiState = viewModel.uiState.value
        assertFalse("Should not be loading", uiState.isLoading)
        assertNull("Portfolio should be null", uiState.portfolio)
        assertNotNull("Error should not be null", uiState.error)
    }

    @Test
    fun `refreshPortfolio should call use case again`() = runTest {
        // Given
        val mockPortfolio = createMockPortfolio()
        coEvery { getPortfolioUseCase() } returns flowOf(mockPortfolio)

        // When
        advanceUntilIdle()
        viewModel.refreshPortfolio()
        advanceUntilIdle()

        // Then
        val uiState = viewModel.uiState.value
        assertNotNull("Portfolio should not be null", uiState.portfolio)
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
                ),
                Stock(
                    symbol = "ICICI",
                    name = "ICICI",
                    ltp = 118.25,
                    avgPrice = 110.0,
                    netQty = 100,
                    profitLoss = 825.0,
                    profitLossPercentage = 7.5
                )
            )
        )
    }
}