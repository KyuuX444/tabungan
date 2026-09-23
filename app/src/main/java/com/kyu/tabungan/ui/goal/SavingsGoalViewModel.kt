package com.kyu.tabungan.ui.goal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyu.tabungan.data.entity.SavingsGoalEntity
import com.kyu.tabungan.data.repository.TabunganRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class SavingsGoalEvent {
    data class ShowMessage(val message: String) : SavingsGoalEvent()
}

class SavingsGoalViewModel(
    private val repository: TabunganRepository
) : ViewModel() {

    val allGoals: StateFlow<List<SavingsGoalEntity>> = repository.getAllSavingsGoals()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _eventFlow = MutableSharedFlow<SavingsGoalEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun saveGoal(
        id: Long = 0L,
        name: String,
        targetAmount: Long,
        savedAmount: Long,
        dailyTarget: Long,
        targetDate: Long? = null,
        imageUri: String? = null,
        icon: String = "phone",
        colorHex: String = "#1687FF",
        note: String = ""
    ) {
        if (name.isBlank()) {
            viewModelScope.launch {
                _eventFlow.emit(SavingsGoalEvent.ShowMessage("Nama target tidak boleh kosong"))
            }
            return
        }
        if (targetAmount <= 0L) {
            viewModelScope.launch {
                _eventFlow.emit(SavingsGoalEvent.ShowMessage("Nominal target harus lebih dari 0"))
            }
            return
        }

        viewModelScope.launch {
            val isAchieved = savedAmount >= targetAmount
            val goal = SavingsGoalEntity(
                id = id,
                name = name.trim(),
                targetAmount = targetAmount,
                savedAmount = savedAmount,
                dailyTarget = dailyTarget,
                targetDate = targetDate,
                imageUri = imageUri,
                icon = icon,
                colorHex = colorHex,
                note = note.trim(),
                isAchieved = isAchieved
            )
            if (id == 0L) {
                repository.insertSavingsGoal(goal)
                _eventFlow.emit(SavingsGoalEvent.ShowMessage("Target tabungan berhasil ditambahkan"))
            } else {
                repository.updateSavingsGoal(goal)
                _eventFlow.emit(SavingsGoalEvent.ShowMessage("Target tabungan berhasil diperbarui"))
            }
        }
    }

    fun addSavings(goalId: Long, amountToAdd: Long) {
        if (amountToAdd <= 0L) return
        viewModelScope.launch {
            repository.addSavedAmountToGoal(goalId, amountToAdd)
            _eventFlow.emit(SavingsGoalEvent.ShowMessage("Berhasil menabung ke target"))
        }
    }

    fun deleteGoal(id: Long) {
        viewModelScope.launch {
            repository.deleteSavingsGoal(id)
            _eventFlow.emit(SavingsGoalEvent.ShowMessage("Target tabungan berhasil dihapus"))
        }
    }
}
