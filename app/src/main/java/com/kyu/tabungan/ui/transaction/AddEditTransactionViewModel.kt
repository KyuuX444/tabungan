package com.kyu.tabungan.ui.transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyu.tabungan.data.entity.CategoryEntity
import com.kyu.tabungan.data.entity.TransactionEntity
import com.kyu.tabungan.data.entity.TransactionType
import com.kyu.tabungan.data.entity.WalletEntity
import com.kyu.tabungan.data.repository.TabunganRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddEditTransactionUiState(
    val transactionId: Long? = null,
    val type: TransactionType = TransactionType.EXPENSE,
    val amount: Long = 0L,
    val selectedCategoryId: Long? = null,
    val selectedWalletId: Long? = null,
    val date: Long = System.currentTimeMillis(),
    val note: String = "",
    val categories: List<CategoryEntity> = emptyList(),
    val wallets: List<WalletEntity> = emptyList(),
    val amountError: String? = null,
    val categoryError: String? = null,
    val walletError: String? = null,
    val isEditMode: Boolean = false,
    val isLoading: Boolean = false
)

sealed class TransactionEvent {
    object SaveSuccess : TransactionEvent()
    data class ShowToast(val message: String) : TransactionEvent()
}

class AddEditTransactionViewModel(
    private val repository: TabunganRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditTransactionUiState())
    val uiState: StateFlow<AddEditTransactionUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<TransactionEvent>()
    val eventFlow: SharedFlow<TransactionEvent> = _eventFlow.asSharedFlow()

    init {
        val passedIdString: String? = savedStateHandle["transactionId"]
        val passedId = passedIdString?.toLongOrNull()

        val passedTypeString: String? = savedStateHandle["type"]
        val passedType = if (!passedTypeString.isNullOrBlank()) {
            try {
                TransactionType.valueOf(passedTypeString)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }

        loadCategoriesAndWallets(passedId, passedType)
    }

    private fun loadCategoriesAndWallets(existingTransactionId: Long?, initialType: TransactionType?) {
        viewModelScope.launch {
            val wallets = repository.getWalletsWithBalance().first().map { it.wallet }
            val categories = repository.getAllCategories().first()

            val effectiveType = initialType ?: TransactionType.EXPENSE
            val defaultWalletId = wallets.firstOrNull()?.id
            val defaultCategoryId = categories.firstOrNull { it.type == effectiveType }?.id

            _uiState.update {
                it.copy(
                    type = effectiveType,
                    wallets = wallets,
                    categories = categories,
                    selectedWalletId = defaultWalletId,
                    selectedCategoryId = defaultCategoryId
                )
            }

            if (existingTransactionId != null && existingTransactionId > 0) {
                val existing = repository.getTransactionEntityById(existingTransactionId)
                if (existing != null) {
                    _uiState.update {
                        it.copy(
                            transactionId = existing.id,
                            type = existing.type,
                            amount = existing.amount,
                            selectedCategoryId = existing.categoryId,
                            selectedWalletId = existing.walletId,
                            date = existing.date,
                            note = existing.note.orEmpty(),
                            isEditMode = true
                        )
                    }
                }
            }
        }
    }

    fun setType(type: TransactionType) {
        val currentCategories = _uiState.value.categories
        val firstMatchingCategory = currentCategories.firstOrNull { it.type == type }?.id
        _uiState.update {
            it.copy(
                type = type,
                selectedCategoryId = firstMatchingCategory,
                categoryError = null
            )
        }
    }

    fun setAmount(amount: Long) {
        _uiState.update { it.copy(amount = amount, amountError = null) }
    }

    fun setCategory(categoryId: Long) {
        _uiState.update { it.copy(selectedCategoryId = categoryId, categoryError = null) }
    }

    fun setWallet(walletId: Long) {
        _uiState.update { it.copy(selectedWalletId = walletId, walletError = null) }
    }

    fun setDate(date: Long) {
        _uiState.update { it.copy(date = date) }
    }

    fun setNote(note: String) {
        _uiState.update { it.copy(note = note) }
    }

    fun saveTransaction() {
        val state = _uiState.value

        var hasError = false
        var amountErr: String? = null
        var categoryErr: String? = null
        var walletErr: String? = null

        if (state.amount <= 0) {
            amountErr = "Nominal harus lebih besar dari 0"
            hasError = true
        }

        if (state.selectedCategoryId == null || state.selectedCategoryId <= 0) {
            categoryErr = "Silakan pilih kategori"
            hasError = true
        }

        if (state.selectedWalletId == null || state.selectedWalletId <= 0) {
            walletErr = "Silakan pilih dompet"
            hasError = true
        }

        if (hasError) {
            _uiState.update {
                it.copy(
                    amountError = amountErr,
                    categoryError = categoryErr,
                    walletError = walletErr
                )
            }
            return
        }

        viewModelScope.launch {
            try {
                if (state.isEditMode && state.transactionId != null) {
                    val updated = TransactionEntity(
                        id = state.transactionId,
                        walletId = state.selectedWalletId!!,
                        categoryId = state.selectedCategoryId!!,
                        type = state.type,
                        amount = state.amount,
                        note = state.note.ifBlank { null },
                        date = state.date
                    )
                    repository.updateTransaction(updated)
                    _eventFlow.emit(TransactionEvent.SaveSuccess)
                } else {
                    val newEntity = TransactionEntity(
                        walletId = state.selectedWalletId!!,
                        categoryId = state.selectedCategoryId!!,
                        type = state.type,
                        amount = state.amount,
                        note = state.note.ifBlank { null },
                        date = state.date
                    )
                    repository.insertTransaction(newEntity)
                    _eventFlow.emit(TransactionEvent.SaveSuccess)
                }
            } catch (e: Exception) {
                _eventFlow.emit(TransactionEvent.ShowToast("Gagal menyimpan: ${e.localizedMessage ?: "Terjadi kesalahan"}"))
            }
        }
    }
}
