package com.portfolio.app.domain.model

data class Stock(
    val symbol: String,
    val name: String,
    val ltp: Double, // Last Traded Price (current price)
    val avgPrice: Double, // Average price (purchase price)
    val netQty: Int,
    val profitLoss: Double,
    val profitLossPercentage: Double,
    val isT1Holding: Boolean = false
) {
    val isProfit: Boolean get() = profitLoss >= 0
    
    val formattedProfitLoss: String get() = 
        "${if (isProfit) "+" else ""}₹${String.format("%.2f", profitLoss)}"
    
    val formattedLtp: String get() = 
        "₹${String.format("%.2f", ltp)}"
    
    val formattedAvgPrice: String get() = 
        "₹${String.format("%.2f", avgPrice)}"
}
