package com.portfolio.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.portfolio.app.presentation.ui.screens.PortfolioScreen
import com.portfolio.app.presentation.ui.theme.PortfolioTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            PortfolioTheme {
                val navController = rememberNavController()
                
                SplashScreen(
                    onSplashFinished = {
                        // Navigate to main content
                    }
                ) {
                    PortfolioScreen(navController = navController)
                }
            }
        }
    }
}

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
    content: @Composable () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }
    var splashFinished by remember { mutableStateOf(false) }
    
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(1000),
        label = "alpha"
    )
    
    val scaleAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.3f,
        animationSpec = tween(1000),
        label = "scale"
    )
    
    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2000)
        splashFinished = true
    }
    
    if (!splashFinished) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1E3A8A)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_portfolio_logo),
                    contentDescription = "Portfolio Logo",
                    modifier = Modifier
                        .size(120.dp)
                        .scale(scaleAnim.value)
                        .alpha(alphaAnim.value)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Portfolio",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    modifier = Modifier.alpha(alphaAnim.value)
                )
            }
        }
    } else {
        content()
    }
}
