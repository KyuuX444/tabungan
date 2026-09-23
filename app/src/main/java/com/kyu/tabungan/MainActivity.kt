package com.kyu.tabungan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
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

        deepLinkUri.value = intent?.data

        setContent {
            TabunganTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val currentUri by deepLinkUri
                LaunchedEffect(currentUri) {
                    val uri = currentUri ?: return@LaunchedEffect
                    when {
                        uri.scheme == "tabungan" && uri.host == "add-expense" -> {
                            navController.navigate(Screen.AddTransaction.createRoute(type = TransactionType.EXPENSE))
                        }
                        uri.scheme == "tabungan" && uri.host == "add-income" -> {
                            navController.navigate(Screen.AddTransaction.createRoute(type = TransactionType.INCOME))
                        }
                        uri.scheme == "tabungan" && uri.host == "statistics" -> {
                            navController.navigate(Screen.Statistics.route)
                        }
                        uri.scheme == "tabungan" && uri.host == "wallet" -> {
                            navController.navigate(Screen.Wallets.route)
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
        deepLinkUri.value = intent.data
    }
}
