package com.portfolio.app.domain.model

data class Portfolio(
    val currentValue: Double,
    val totalInvestment: Double,
    val todaysProfitLoss: Double,
    val overallProfitLoss: Double,
    val overallProfitLossPercentage: Double,
    val stocks: List<Stock>
) {
    val isOverallProfit: Boolean get() = overallProfitLoss >= 0
    val isTodaysProfit: Boolean get() = todaysProfitLoss >= 0
    
    val formattedCurrentValue: String get() = 
        "₹${String.format("%.2f", currentValue)}"
    
    val formattedTotalInvestment: String get() = 
        "₹${String.format("%.2f", totalInvestment)}"
    
    val formattedTodaysProfitLoss: String get() = 
        "${if (isTodaysProfit) "+" else ""}₹${String.format("%.2f", todaysProfitLoss)}"
    
    val formattedOverallProfitLoss: String get() = 
        "${if (isOverallProfit) "+" else ""}₹${String.format("%.2f", overallProfitLoss)}"
    
    val formattedOverallProfitLossPercentage: String get() = 
        "${if (isOverallProfit) "+" else ""}${String.format("%.2f", overallProfitLossPercentage)}%"
}
