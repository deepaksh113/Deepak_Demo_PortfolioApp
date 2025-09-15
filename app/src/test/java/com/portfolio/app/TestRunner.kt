package com.portfolio.app

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    com.portfolio.app.data.repository.PortfolioRepositoryImplTest::class,
    com.portfolio.app.presentation.viewmodel.PortfolioViewModelTest::class,
    com.portfolio.app.domain.usecase.GetPortfolioUseCaseTest::class
)
class TestRunner
