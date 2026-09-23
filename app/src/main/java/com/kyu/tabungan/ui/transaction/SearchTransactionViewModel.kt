package com.kyu.tabungan.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyu.tabungan.data.entity.TransactionType
import com.kyu.tabungan.data.model.TransactionItemModel
import com.kyu.tabungan.data.repository.TabunganRepository
import com.kyu.tabungan.util.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.util.Calendar

enum class DateFilterType {
    ALL,
    TODAY,
    THIS_WEEK,
    THIS_MONTH,
    LAST_MONTH
}

data class SearchUiState(
    val query: String = "",
    val typeFilter: TransactionType? = null,
    val dateFilter: DateFilterType = DateFilterType.ALL,
    val customStartDate: Long? = null,
    val customEndDate: Long? = null
)

class SearchTransactionViewModel(
    private val repository: TabunganRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    val searchResults: StateFlow<List<TransactionItemModel>> = _uiState.flatMapLatest { state ->
        val (startDate, endDate) = calculateDateRange(state.dateFilter, state.customStartDate, state.customEndDate)
        repository.searchTransactions(
            query = state.query,
            type = state.typeFilter,
            walletId = null,
            categoryId = null,
            startDate = startDate,
            endDate = endDate
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setQuery(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    fun setTypeFilter(type: TransactionType?) {
        _uiState.update {
            it.copy(typeFilter = if (it.typeFilter == type) null else type)
        }
    }

    fun setDateFilter(filter: DateFilterType) {
        _uiState.update {
            it.copy(dateFilter = if (it.dateFilter == filter) DateFilterType.ALL else filter)
        }
    }

    private fun calculateDateRange(
        filter: DateFilterType,
        customStart: Long?,
        customEnd: Long?
    ): Pair<Long?, Long?> {
        val now = System.currentTimeMillis()
        val cal = Calendar.getInstance()
        return when (filter) {
            DateFilterType.ALL -> Pair(null, null)
            DateFilterType.TODAY -> DateUtils.getDayRange(now)
            DateFilterType.THIS_WEEK -> DateUtils.getWeekRange(cal)
            DateFilterType.THIS_MONTH -> {
                val m = cal.get(Calendar.MONTH) + 1
                val y = cal.get(Calendar.YEAR)
                DateUtils.getMonthRange(m, y)
            }
            DateFilterType.LAST_MONTH -> {
                cal.add(Calendar.MONTH, -1)
                val m = cal.get(Calendar.MONTH) + 1
                val y = cal.get(Calendar.YEAR)
                DateUtils.getMonthRange(m, y)
            }
        }
    }
}
