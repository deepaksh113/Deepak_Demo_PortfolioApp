package com.portfolio.app.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.portfolio.app.R
import com.portfolio.app.presentation.ui.components.PortfolioSummary
import com.portfolio.app.presentation.ui.components.StockCard
import com.portfolio.app.presentation.ui.theme.BackgroundGray
import com.portfolio.app.presentation.ui.theme.LossRed
import com.portfolio.app.presentation.ui.theme.PortfolioBlue
import com.portfolio.app.presentation.ui.theme.TextPrimary
import com.portfolio.app.presentation.ui.theme.TextSecondary
import com.portfolio.app.presentation.ui.theme.White
import com.portfolio.app.presentation.viewmodel.PortfolioViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun PortfolioScreen(
    navController: NavController,
    viewModel: PortfolioViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Portfolio",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle sort */ }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_swap_vert_24),
                            contentDescription = "Sort",
                            tint = White
                        )
                    }
                    IconButton(onClick = { /* Handle search */ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PortfolioBlue
                )
            )
        },
        bottomBar = {
            BottomNavigationBar()
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundGray)
                .padding(paddingValues)
        ) {
            // Tab Row
            TabRow(
                selectedTabIndex = 0,
                containerColor = White,
                contentColor = TextPrimary
            ) {
                Tab(
                    selected = true,
                    onClick = { },
                    text = {
                        Text(
                            text = "POSITIONS",
                            style = MaterialTheme.typography.labelLarge,
                            color = TextSecondary
                        )
                    }
                )
                Tab(
                    selected = true,
                    onClick = { },
                    text = {
                        Text(
                            text = "HOLDINGS",
                            style = MaterialTheme.typography.labelLarge,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            }
            
            // Content with Pull to Refresh
            val pullRefreshState = rememberPullRefreshState(
                refreshing = uiState.isLoading,
                onRefresh = { viewModel.refreshPortfolio() }
            )
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
            ) {
                when {
                    uiState.error != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Error: ${uiState.error}",
                                    color = LossRed,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { viewModel.refreshPortfolio() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = PortfolioBlue
                                    )
                                ) {
                                    Text("Retry", color = White)
                                }
                            }
                        }
                    }
                    
                    uiState.portfolio != null -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(uiState.portfolio!!.stocks) { stock ->
                                StockCard(stock = stock)
                            }
                            
                            item {
                                PortfolioSummary(portfolio = uiState.portfolio!!)
                            }
                        }
                    }
                }
                
                PullRefreshIndicator(
                    refreshing = uiState.isLoading,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar() {
    NavigationBar(
        containerColor = White,
        contentColor = TextSecondary
    ) {
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "Watchlist"
                )
            },
            label = {
                Text(
                    text = "Watchlist",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        )
        
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_schedule_24),
                    contentDescription = "Orders"
                )
            },
            label = {
                Text(
                    text = "Orders",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        )
        
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_work_outline_24),
                    contentDescription = "Portfolio"
                )
            },
            label = {
                Text(
                    text = "Portfolio",
                    style = MaterialTheme.typography.labelSmall,
                    color = PortfolioBlue
                )
            }
        )
        
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_currency_rupee_24),
                    contentDescription = "Funds"
                )
            },
            label = {
                Text(
                    text = "Funds",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        )
        
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable._275_money_bag_rupee),
                    contentDescription = "Invest"
                )
            },
            label = {
                Text(
                    text = "Invest",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        )
    }
}
