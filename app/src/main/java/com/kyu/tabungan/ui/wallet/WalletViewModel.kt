package com.kyu.tabungan.ui.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyu.tabungan.data.entity.WalletEntity
import com.kyu.tabungan.data.entity.WalletType
import com.kyu.tabungan.data.model.WalletWithBalance
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

sealed class WalletEvent {
    data class ShowMessage(val message: String) : WalletEvent()
}

class WalletViewModel(
    private val repository: TabunganRepository
) : ViewModel() {

    val walletsWithBalance: StateFlow<List<WalletWithBalance>> = repository.getWalletsWithBalance()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalBalance: StateFlow<Long> = repository.getTotalBalance()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    private val _eventFlow = MutableSharedFlow<WalletEvent>()
    val eventFlow: SharedFlow<WalletEvent> = _eventFlow.asSharedFlow()

    fun saveWallet(id: Long = 0L, name: String, type: WalletType, initialBalance: Long, icon: String) {
        viewModelScope.launch {
            if (name.isBlank()) {
                _eventFlow.emit(WalletEvent.ShowMessage("Nama dompet tidak boleh kosong"))
                return@launch
            }

            if (id > 0L) {
                repository.updateWallet(
                    WalletEntity(
                        id = id,
                        name = name.trim(),
                        type = type,
                        initialBalance = initialBalance,
                        icon = icon
                    )
                )
            } else {
                repository.insertWallet(
                    WalletEntity(
                        name = name.trim(),
                        type = type,
                        initialBalance = initialBalance,
                        icon = icon
                    )
                )
            }
        }
    }

    fun deleteWallet(walletId: Long) {
        viewModelScope.launch {
            val result = repository.deleteWallet(walletId)
            result.onFailure {
                _eventFlow.emit(WalletEvent.ShowMessage(it.localizedMessage ?: "Gagal menghapus dompet"))
            }
        }
    }
}
