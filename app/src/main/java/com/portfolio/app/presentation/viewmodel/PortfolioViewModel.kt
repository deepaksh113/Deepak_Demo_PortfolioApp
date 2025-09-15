package com.portfolio.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.portfolio.app.domain.model.Portfolio
import com.portfolio.app.domain.usecase.GetPortfolioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PortfolioViewModel @Inject constructor(
    private val getPortfolioUseCase: GetPortfolioUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(PortfolioUiState())
    val uiState: StateFlow<PortfolioUiState> = _uiState.asStateFlow()
    
    init {
        loadPortfolio()
    }
    
    private fun loadPortfolio() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                getPortfolioUseCase().collect { portfolio ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        portfolio = portfolio,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                )
            }
        }
    }
    
    fun refreshPortfolio() {
        loadPortfolio()
    }
}

data class PortfolioUiState(
    val isLoading: Boolean = false,
    val portfolio: Portfolio? = null,
    val error: String? = null
)
