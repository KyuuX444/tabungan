package com.kyu.tabungan.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyu.tabungan.data.model.CategorySpending
import com.kyu.tabungan.data.model.FinancialSummary
import com.kyu.tabungan.data.repository.TabunganRepository
import com.kyu.tabungan.util.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.util.Calendar

enum class StatisticsPeriod {
    WEEK,
    MONTH,
    YEAR
}

data class StatisticsUiState(
    val period: StatisticsPeriod = StatisticsPeriod.MONTH,
    val calendar: Calendar = Calendar.getInstance(),
    val periodLabel: String = ""
)

class StatisticsViewModel(
    private val repository: TabunganRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        StatisticsUiState(
            periodLabel = DateUtils.formatMonthYear(
                Calendar.getInstance().get(Calendar.MONTH) + 1,
                Calendar.getInstance().get(Calendar.YEAR)
            )
        )
    )
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    val financialSummary: StateFlow<FinancialSummary> = _uiState.flatMapLatest { state ->
        val range = calculateDateRange(state.period, state.calendar)
        repository.getFinancialSummary(range.first, range.second)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FinancialSummary())

    val categorySpendings: StateFlow<List<CategorySpending>> = _uiState.flatMapLatest { state ->
        val range = calculateDateRange(state.period, state.calendar)
        repository.getCategorySpendings(range.first, range.second)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setPeriod(period: StatisticsPeriod) {
        _uiState.update {
            val updated = it.copy(period = period)
            updated.copy(periodLabel = generatePeriodLabel(updated.period, updated.calendar))
        }
    }

    fun previousPeriod() {
        val cal = _uiState.value.calendar.clone() as Calendar
        when (_uiState.value.period) {
            StatisticsPeriod.WEEK -> cal.add(Calendar.WEEK_OF_YEAR, -1)
            StatisticsPeriod.MONTH -> cal.add(Calendar.MONTH, -1)
            StatisticsPeriod.YEAR -> cal.add(Calendar.YEAR, -1)
        }
        _uiState.update {
            it.copy(calendar = cal, periodLabel = generatePeriodLabel(it.period, cal))
        }
    }

    fun nextPeriod() {
        val cal = _uiState.value.calendar.clone() as Calendar
        when (_uiState.value.period) {
            StatisticsPeriod.WEEK -> cal.add(Calendar.WEEK_OF_YEAR, 1)
            StatisticsPeriod.MONTH -> cal.add(Calendar.MONTH, 1)
            StatisticsPeriod.YEAR -> cal.add(Calendar.YEAR, 1)
        }
        _uiState.update {
            it.copy(calendar = cal, periodLabel = generatePeriodLabel(it.period, cal))
        }
    }

    private fun calculateDateRange(period: StatisticsPeriod, cal: Calendar): Pair<Long, Long> {
        return when (period) {
            StatisticsPeriod.WEEK -> DateUtils.getWeekRange(cal)
            StatisticsPeriod.MONTH -> {
                val m = cal.get(Calendar.MONTH) + 1
                val y = cal.get(Calendar.YEAR)
                DateUtils.getMonthRange(m, y)
            }
            StatisticsPeriod.YEAR -> {
                val y = cal.get(Calendar.YEAR)
                DateUtils.getYearRange(y)
            }
        }
    }

    private fun generatePeriodLabel(period: StatisticsPeriod, cal: Calendar): String {
        return when (period) {
            StatisticsPeriod.WEEK -> {
                val (start, end) = DateUtils.getWeekRange(cal)
                "${DateUtils.formatShortDate(start)} - ${DateUtils.formatShortDate(end)}"
            }
            StatisticsPeriod.MONTH -> {
                val m = cal.get(Calendar.MONTH) + 1
                val y = cal.get(Calendar.YEAR)
                DateUtils.formatMonthYear(m, y)
            }
            StatisticsPeriod.YEAR -> {
                cal.get(Calendar.YEAR).toString()
            }
        }
    }
}
