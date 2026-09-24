package com.kyu.tabungan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kyu.tabungan.components.icons.NeoIcons
import com.kyu.tabungan.data.entity.TransactionType
import com.kyu.tabungan.navigation.Screen
import com.kyu.tabungan.navigation.TabunganBottomBar
import com.kyu.tabungan.navigation.TabunganNavHost
import com.kyu.tabungan.theme.Background
import com.kyu.tabungan.theme.BorderColor
import com.kyu.tabungan.theme.BrightBlue
import com.kyu.tabungan.theme.HardShadowColor
import com.kyu.tabungan.theme.Surface
import com.kyu.tabungan.theme.TabunganTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private val deepLinkUri = mutableStateOf<Uri?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        val app = application as TabunganApp
        val repository = app.repository

        val initialUri = intent?.data ?: intent?.getStringExtra("route")?.let { Uri.parse(it) }
        deepLinkUri.value = initialUri

        setContent {
            TabunganTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val currentUri by deepLinkUri
                LaunchedEffect(currentUri) {
                    val uri = currentUri ?: return@LaunchedEffect
                    deepLinkUri.value = null

                    val target = uri.host?.takeIf { it.isNotBlank() }
                        ?: uri.path?.removePrefix("/")?.takeIf { it.isNotBlank() }
                        ?: uri.toString().substringAfter("://").substringBefore("/")

                    when (target) {
                        "add-expense", "expense" -> {
                            navController.navigate(Screen.AddTransaction.createRoute(type = TransactionType.EXPENSE)) {
                                launchSingleTop = true
                            }
                        }
                        "add-income", "income" -> {
                            navController.navigate(Screen.AddTransaction.createRoute(type = TransactionType.INCOME)) {
                                launchSingleTop = true
                            }
                        }
                        "statistics", "stats" -> {
                            navController.navigate(Screen.Statistics.route) {
                                popUpTo(Screen.Home.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        "wallet", "wallets" -> {
                            navController.navigate(Screen.Wallets.route) {
                                popUpTo(Screen.Home.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        "savings-goals", "goals", "tabungan" -> {
                            navController.navigate(Screen.SavingsGoals.route) {
                                launchSingleTop = true
                            }
                        }
                        "home" -> {
                            if (navController.currentDestination?.route != Screen.Home.route) {
                                navController.popBackStack(Screen.Home.route, false)
                            }
                        }
                    }
                }

                var isSplashVisible by remember { mutableStateOf(true) }
                LaunchedEffect(Unit) {
                    delay(950)
                    isSplashVisible = false
                }

                val isTopLevelDestination = currentRoute in listOf(
                    Screen.Home.route,
                    Screen.Statistics.route,
                    Screen.Wallets.route,
                    Screen.More.route
                )

                Box(modifier = Modifier.fillMaxSize()) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = Background,
                        bottomBar = {
                            if (isTopLevelDestination) {
                                TabunganBottomBar(
                                    currentRoute = currentRoute,
                                    onNavigate = { route ->
                                        navController.navigate(route) {
                                            popUpTo(Screen.Home.route) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    onAddClick = {
                                        navController.navigate(Screen.AddTransaction.createRoute())
                                    }
                                )
                            }
                        }
                    ) { paddingValues ->
                        TabunganNavHost(
                            navController = navController,
                            repository = repository,
                            paddingValues = paddingValues
                        )
                    }

                    AnimatedVisibility(
                        visible = isSplashVisible,
                        exit = fadeOut(animationSpec = tween(350))
                    ) {
                        TabunganSplashScreen()
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        val uri = intent.data ?: intent.getStringExtra("route")?.let { Uri.parse(it) }
        deepLinkUri.value = uri
    }
}

@Composable
private fun TabunganSplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrightBlue),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val logoShape = RoundedCornerShape(22.dp)
            Box(
                modifier = Modifier.padding(bottom = 6.dp, end = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .offset(x = 6.dp, y = 6.dp)
                        .background(HardShadowColor, shape = logoShape)
                )
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(logoShape)
                        .background(Surface)
                        .border(width = 3.dp, color = BorderColor, shape = logoShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = NeoIcons.Wallet,
                        contentDescription = null,
                        tint = BrightBlue,
                        modifier = Modifier.size(54.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Tabungan",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = Surface,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Kelola Keuangan & Impian",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Surface.copy(alpha = 0.9f)
            )
        }
    }
}
