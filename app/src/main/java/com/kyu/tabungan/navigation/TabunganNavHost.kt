package com.kyu.tabungan.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kyu.tabungan.data.repository.TabunganRepository
import com.kyu.tabungan.ui.budget.BudgetScreen
import com.kyu.tabungan.ui.budget.BudgetViewModel
import com.kyu.tabungan.ui.category.CategoryScreen
import com.kyu.tabungan.ui.category.CategoryViewModel
import com.kyu.tabungan.ui.home.HomeScreen
import com.kyu.tabungan.ui.home.HomeViewModel
import com.kyu.tabungan.ui.more.MoreScreen
import com.kyu.tabungan.ui.settings.SettingsScreen
import com.kyu.tabungan.ui.settings.SettingsViewModel
import com.kyu.tabungan.ui.statistics.StatisticsScreen
import com.kyu.tabungan.ui.statistics.StatisticsViewModel
import com.kyu.tabungan.ui.transaction.AddEditTransactionScreen
import com.kyu.tabungan.ui.transaction.AddEditTransactionViewModel
import com.kyu.tabungan.ui.transaction.SearchTransactionScreen
import com.kyu.tabungan.ui.transaction.SearchTransactionViewModel
import com.kyu.tabungan.ui.transaction.TransactionDetailScreen
import com.kyu.tabungan.ui.transaction.TransactionDetailViewModel
import com.kyu.tabungan.ui.wallet.WalletScreen
import com.kyu.tabungan.ui.wallet.WalletViewModel

class TabunganViewModelFactory(
    private val repository: TabunganRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository) as T
            modelClass.isAssignableFrom(StatisticsViewModel::class.java) -> StatisticsViewModel(repository) as T
            modelClass.isAssignableFrom(WalletViewModel::class.java) -> WalletViewModel(repository) as T
            modelClass.isAssignableFrom(BudgetViewModel::class.java) -> BudgetViewModel(repository) as T
            modelClass.isAssignableFrom(CategoryViewModel::class.java) -> CategoryViewModel(repository) as T
            modelClass.isAssignableFrom(SearchTransactionViewModel::class.java) -> SearchTransactionViewModel(repository) as T
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

@Composable
fun TabunganNavHost(
    navController: NavHostController,
    repository: TabunganRepository,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier
) {
    val factory = TabunganViewModelFactory(repository)

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier.padding(paddingValues)
    ) {
        composable(Screen.Home.route) {
            val vm: HomeViewModel = viewModel(factory = factory)
            HomeScreen(
                viewModel = vm,
                onNavigateToAddTransaction = { navController.navigate(Screen.AddTransaction.createRoute()) },
                onNavigateToTransactionDetail = { id -> navController.navigate(Screen.TransactionDetail.createRoute(id)) },
                onNavigateToSearch = { navController.navigate(Screen.Search.route) },
                onNavigateToBudgets = { navController.navigate(Screen.Budgets.route) }
            )
        }

        composable(Screen.Statistics.route) {
            val vm: StatisticsViewModel = viewModel(factory = factory)
            StatisticsScreen(viewModel = vm)
        }

        composable(Screen.Wallets.route) {
            val vm: WalletViewModel = viewModel(factory = factory)
            WalletScreen(viewModel = vm)
        }

        composable(Screen.More.route) {
            MoreScreen(
                onNavigateToBudgets = { navController.navigate(Screen.Budgets.route) },
                onNavigateToCategories = { navController.navigate(Screen.Categories.route) },
                onNavigateToSearch = { navController.navigate(Screen.Search.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }

        composable(Screen.Budgets.route) {
            val vm: BudgetViewModel = viewModel(factory = factory)
            BudgetScreen(
                viewModel = vm,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Categories.route) {
            val vm: CategoryViewModel = viewModel(factory = factory)
            CategoryScreen(
                viewModel = vm,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Search.route) {
            val vm: SearchTransactionViewModel = viewModel(factory = factory)
            SearchTransactionScreen(
                viewModel = vm,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { id -> navController.navigate(Screen.TransactionDetail.createRoute(id)) }
            )
        }

        composable(Screen.Settings.route) {
            val vm: SettingsViewModel = viewModel(factory = factory)
            SettingsScreen(
                viewModel = vm,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCategories = { navController.navigate(Screen.Categories.route) }
            )
        }

        composable(
            route = "add_transaction?transactionId={transactionId}&type={type}",
            arguments = listOf(
                navArgument("transactionId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("type") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val savedStateHandle = backStackEntry.savedStateHandle
            val vm = viewModel<AddEditTransactionViewModel>(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return AddEditTransactionViewModel(repository, savedStateHandle) as T
                    }
                }
            )
            AddEditTransactionScreen(
                viewModel = vm,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "transaction_detail/{transactionId}",
            arguments = listOf(
                navArgument("transactionId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val savedStateHandle = backStackEntry.savedStateHandle
            val vm = viewModel<TransactionDetailViewModel>(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return TransactionDetailViewModel(repository, savedStateHandle) as T
                    }
                }
            )
            TransactionDetailScreen(
                viewModel = vm,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { id -> navController.navigate(Screen.AddTransaction.createRoute(id)) }
            )
        }
    }
}
