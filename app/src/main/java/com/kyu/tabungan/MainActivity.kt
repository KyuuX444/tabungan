package com.kyu.tabungan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kyu.tabungan.data.entity.TransactionType
import com.kyu.tabungan.navigation.Screen
import com.kyu.tabungan.navigation.TabunganBottomBar
import com.kyu.tabungan.navigation.TabunganNavHost
import com.kyu.tabungan.theme.Background
import com.kyu.tabungan.theme.TabunganTheme

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
                LaunchedEffect(currentUri, navBackStackEntry) {
                    val uri = currentUri ?: return@LaunchedEffect
                    if (navBackStackEntry == null) return@LaunchedEffect

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
                                launchSingleTop = true
                            }
                        }
                        "wallet", "wallets" -> {
                            navController.navigate(Screen.Wallets.route) {
                                launchSingleTop = true
                            }
                        }
                        "savings-goals", "goals" -> {
                            navController.navigate(Screen.SavingsGoals.route) {
                                launchSingleTop = true
                            }
                        }
                        "home" -> {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }
                    deepLinkUri.value = null
                }

                val isTopLevelDestination = currentRoute in listOf(
                    Screen.Home.route,
                    Screen.Statistics.route,
                    Screen.Wallets.route,
                    Screen.More.route
                )

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
