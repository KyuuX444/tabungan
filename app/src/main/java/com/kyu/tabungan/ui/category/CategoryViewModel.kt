package com.kyu.tabungan.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyu.tabungan.data.entity.CategoryEntity
import com.kyu.tabungan.data.entity.TransactionType
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

sealed class CategoryEvent {
    data class ShowMessage(val message: String) : CategoryEvent()
}

class CategoryViewModel(
    private val repository: TabunganRepository
) : ViewModel() {

    val allCategories: StateFlow<List<CategoryEntity>> = repository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _eventFlow = MutableSharedFlow<CategoryEvent>()
    val eventFlow: SharedFlow<CategoryEvent> = _eventFlow.asSharedFlow()

    fun saveCategory(id: Long = 0L, name: String, type: TransactionType, icon: String) {
        viewModelScope.launch {
            if (name.isBlank()) {
                _eventFlow.emit(CategoryEvent.ShowMessage("Nama kategori tidak boleh kosong"))
                return@launch
            }

            if (id > 0L) {
                repository.updateCategory(
                    CategoryEntity(
                        id = id,
                        name = name.trim(),
                        type = type,
                        icon = icon,
                        isDefault = false
                    )
                )
            } else {
                repository.insertCategory(
                    CategoryEntity(
                        name = name.trim(),
                        type = type,
                        icon = icon,
                        isDefault = false
                    )
                )
            }
        }
    }

    fun deleteCategory(categoryId: Long) {
        viewModelScope.launch {
            val result = repository.deleteCategory(categoryId)
            result.onFailure {
                _eventFlow.emit(CategoryEvent.ShowMessage(it.localizedMessage ?: "Gagal menghapus kategori"))
            }
        }
    }
}
