package com.kyu.tabungan.ui.transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyu.tabungan.data.model.TransactionItemModel
import com.kyu.tabungan.data.repository.TabunganRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class DetailEvent {
    object DeletedSuccess : DetailEvent()
}

class TransactionDetailViewModel(
    private val repository: TabunganRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val transactionId: Long = checkNotNull(savedStateHandle["transactionId"]).toString().toLong()

    val transaction: StateFlow<TransactionItemModel?> = repository.getTransactionById(transactionId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _eventFlow = MutableSharedFlow<DetailEvent>()
    val eventFlow: SharedFlow<DetailEvent> = _eventFlow.asSharedFlow()

    fun deleteTransaction() {
        viewModelScope.launch {
            repository.deleteTransaction(transactionId)
            _eventFlow.emit(DetailEvent.DeletedSuccess)
        }
    }
}
