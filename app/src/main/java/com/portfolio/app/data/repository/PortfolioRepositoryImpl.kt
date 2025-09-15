package com.portfolio.app.data.repository

import com.portfolio.app.data.lib.ApiService
import com.portfolio.app.domain.model.Portfolio
import com.portfolio.app.domain.model.Stock
import com.portfolio.app.domain.repository.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PortfolioRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : PortfolioRepository {
    
    override fun getPortfolio(): Flow<Portfolio> = flow {
        try {
            val response = apiService.getPortfolioData()
            val portfolio = mapToPortfolio(response)
            emit(portfolio)
        } catch (e: Exception) {
            throw e
        }
    }
    
    private fun mapToPortfolio(response: com.portfolio.app.data.lib.dto.PortfolioResponseDto): Portfolio {
        val stocks = response.data.userHolding.map { holding ->
            val currentValue = holding.ltp * holding.quantity
            val investmentValue = holding.avgPrice * holding.quantity
            val profitLoss = currentValue - investmentValue
            val profitLossPercentage = if (investmentValue > 0) {
                (profitLoss / investmentValue) * 100
            } else 0.0
            
            Stock(
                symbol = holding.symbol,
                name = holding.symbol,
                ltp = holding.ltp,
                avgPrice = holding.avgPrice,
                netQty = holding.quantity,
                profitLoss = profitLoss,
                profitLossPercentage = profitLossPercentage
            )
        }
        
        val currentValue = stocks.sumOf { it.ltp * it.netQty }
        val totalInvestment = stocks.sumOf { it.avgPrice * it.netQty }
        val overallProfitLoss = currentValue - totalInvestment
        val overallProfitLossPercentage = if (totalInvestment > 0) {
            (overallProfitLoss / totalInvestment) * 100
        } else 0.0
        
        // Calculate today's P&L (difference between LTP and close price)
        val todaysProfitLoss = stocks.sumOf { stock ->
            val holding = response.data.userHolding.find { it.symbol == stock.symbol }
            if (holding != null) {
                (holding.close - stock.ltp) * stock.netQty
            } else 0.0
        }
        
        return Portfolio(
            currentValue = currentValue,
            totalInvestment = totalInvestment,
            todaysProfitLoss = todaysProfitLoss,
            overallProfitLoss = overallProfitLoss,
            overallProfitLossPercentage = overallProfitLossPercentage,
            stocks = stocks
        )
    }
}
