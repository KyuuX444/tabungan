package com.kyu.tabungan.ui.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyu.tabungan.data.entity.BudgetEntity
import com.kyu.tabungan.data.entity.CategoryEntity
import com.kyu.tabungan.data.entity.TransactionType
import com.kyu.tabungan.data.model.BudgetWithUsage
import com.kyu.tabungan.data.repository.TabunganRepository
import com.kyu.tabungan.util.DateUtils
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

data class BudgetUiState(
    val month: Int = Calendar.getInstance().get(Calendar.MONTH) + 1,
    val year: Int = Calendar.getInstance().get(Calendar.YEAR),
    val totalLimit: Long = 0L,
    val totalSpent: Long = 0L,
    val budgets: List<BudgetWithUsage> = emptyList(),
    val expenseCategories: List<CategoryEntity> = emptyList()
)

sealed class BudgetEvent {
    data class ShowMessage(val message: String) : BudgetEvent()
}

class BudgetViewModel(
    private val repository: TabunganRepository
) : ViewModel() {

    private val cal = Calendar.getInstance()
    private val currentMonth = cal.get(Calendar.MONTH) + 1
    private val currentYear = cal.get(Calendar.YEAR)
    private val monthRange = DateUtils.getMonthRange(currentMonth, currentYear)

    private val _expenseCategories = MutableStateFlow<List<CategoryEntity>>(emptyList())
    val expenseCategories: StateFlow<List<CategoryEntity>> = _expenseCategories.asStateFlow()

    private val _eventFlow = MutableSharedFlow<BudgetEvent>()
    val eventFlow: SharedFlow<BudgetEvent> = _eventFlow.asSharedFlow()

    val budgetsWithUsage: StateFlow<List<BudgetWithUsage>> = repository.getBudgetsWithUsage(
        currentMonth,
        currentYear,
        monthRange.first,
        monthRange.second
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalBudgetLimit: StateFlow<Long> = repository.getTotalBudgetLimit(currentMonth, currentYear)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    val uiState: StateFlow<BudgetUiState> = combine(
        budgetsWithUsage,
        totalBudgetLimit,
        expenseCategories
    ) { budgets, limit, categories ->
        val spent = budgets.sumOf { it.spentAmount }
        BudgetUiState(
            month = currentMonth,
            year = currentYear,
            totalLimit = limit,
            totalSpent = spent,
            budgets = budgets,
            expenseCategories = categories
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BudgetUiState())

    init {
        loadExpenseCategories()
    }

    private fun loadExpenseCategories() {
        viewModelScope.launch {
            repository.getCategoriesByType(TransactionType.EXPENSE).collect {
                _expenseCategories.value = it
            }
        }
    }

    fun saveBudget(id: Long = 0L, categoryId: Long, limitAmount: Long) {
        viewModelScope.launch {
            if (limitAmount <= 0) {
                _eventFlow.emit(BudgetEvent.ShowMessage("Batas anggaran harus lebih besar dari 0"))
                return@launch
            }

            if (id > 0L) {
                repository.updateBudget(
                    BudgetEntity(
                        id = id,
                        categoryId = categoryId,
                        month = currentMonth,
                        year = currentYear,
                        limitAmount = limitAmount
                    )
                )
            } else {
                repository.insertBudget(
                    BudgetEntity(
                        categoryId = categoryId,
                        month = currentMonth,
                        year = currentYear,
                        limitAmount = limitAmount
                    )
                )
            }
        }
    }

    fun deleteBudget(id: Long) {
        viewModelScope.launch {
            repository.deleteBudget(id)
        }
    }
}
