package com.kyu.tabungan.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyu.tabungan.data.model.FinancialSummary
import com.kyu.tabungan.data.model.TransactionItemModel
import com.kyu.tabungan.data.repository.TabunganRepository
import com.kyu.tabungan.util.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar

data class HomeUiState(
    val totalBalance: Long = 0L,
    val monthlyIncome: Long = 0L,
    val monthlyExpense: Long = 0L,
    val monthlyBudgetLimit: Long = 0L,
    val monthlyBudgetSpent: Long = 0L,
    val activeGoal: com.kyu.tabungan.data.entity.SavingsGoalEntity? = null,
    val recentTransactions: List<TransactionItemModel> = emptyList(),
    val isLoading: Boolean = false
)

class HomeViewModel(
    private val repository: TabunganRepository
) : ViewModel() {

    private val cal = Calendar.getInstance()
    private val currentMonth = cal.get(Calendar.MONTH) + 1
    private val currentYear = cal.get(Calendar.YEAR)
    private val monthRange = DateUtils.getMonthRange(currentMonth, currentYear)

    val totalBalance = repository.getTotalBalance()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    val financialSummary = repository.getFinancialSummary(monthRange.first, monthRange.second)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FinancialSummary())

    val recentTransactions = repository.getRecentTransactions(10)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalBudgetLimit = repository.getTotalBudgetLimit(currentMonth, currentYear)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    val budgetsWithUsage = repository.getBudgetsWithUsage(currentMonth, currentYear, monthRange.first, monthRange.second)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val budgetInfo = combine(totalBudgetLimit, budgetsWithUsage) { limit, budgets ->
        Pair(limit, budgets.sumOf { it.spentAmount })
    }

    val allGoals = repository.getAllSavingsGoals()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val uiState: StateFlow<HomeUiState> = combine(
        totalBalance,
        financialSummary,
        recentTransactions,
        budgetInfo,
        allGoals
    ) { balance, summary, recent, budgetData, goals ->
        val (budgetLimit, spentOnBudgets) = budgetData
        val active = goals.firstOrNull { !it.isAchieved } ?: goals.firstOrNull()
        HomeUiState(
            totalBalance = balance,
            monthlyIncome = summary.totalIncome,
            monthlyExpense = summary.totalExpense,
            monthlyBudgetLimit = budgetLimit,
            monthlyBudgetSpent = spentOnBudgets,
            activeGoal = active,
            recentTransactions = recent,
            isLoading = false
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())
}
