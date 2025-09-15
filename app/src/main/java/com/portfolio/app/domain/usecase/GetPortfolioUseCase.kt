package com.portfolio.app.domain.usecase

import com.portfolio.app.domain.model.Portfolio
import com.portfolio.app.domain.repository.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPortfolioUseCase @Inject constructor(
    private val portfolioRepository: PortfolioRepository
) {
    operator fun invoke(): Flow<Portfolio> = portfolioRepository.getPortfolio()
}
