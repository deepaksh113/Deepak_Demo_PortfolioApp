package com.portfolio.app.domain.repository

import com.portfolio.app.domain.model.Portfolio
import kotlinx.coroutines.flow.Flow

interface PortfolioRepository {
    fun getPortfolio(): Flow<Portfolio>
}
