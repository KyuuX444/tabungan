package com.kyu.tabungan.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyu.tabungan.data.backup.BackupData
import com.kyu.tabungan.data.backup.BackupManager
import com.kyu.tabungan.data.backup.BackupSummary
import com.kyu.tabungan.data.repository.TabunganRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class SettingsEvent {
    data class ShowMessage(val message: String) : SettingsEvent()
    data class ShareExportJson(val jsonString: String, val filename: String) : SettingsEvent()
}

data class SettingsUiState(
    val pendingImportData: BackupData? = null,
    val pendingImportSummary: BackupSummary? = null,
    val isImportDialogVisible: Boolean = false,
    val isLoading: Boolean = false
)

class SettingsViewModel(
    private val repository: TabunganRepository
) : ViewModel() {

    private val backupManager = BackupManager()

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<SettingsEvent>()
    val eventFlow: SharedFlow<SettingsEvent> = _eventFlow.asSharedFlow()

    fun exportData() {
        viewModelScope.launch {
            try {
                val backup = repository.exportBackup()
                val jsonString = backupManager.exportToJson(backup)
                _eventFlow.emit(
                    SettingsEvent.ShareExportJson(
                        jsonString = jsonString,
                        filename = "tabungan_backup.json"
                    )
                )
            } catch (e: Exception) {
                _eventFlow.emit(SettingsEvent.ShowMessage("Gagal mengekspor data: ${e.localizedMessage ?: "Terjadi kesalahan"}"))
            }
        }
    }

    fun onImportJsonSelected(jsonString: String) {
        val result = backupManager.parseAndValidate(jsonString)
        result.fold(
            onSuccess = { (data, summary) ->
                _uiState.update {
                    it.copy(
                        pendingImportData = data,
                        pendingImportSummary = summary,
                        isImportDialogVisible = true
                    )
                }
            },
            onFailure = { error ->
                viewModelScope.launch {
                    _eventFlow.emit(SettingsEvent.ShowMessage(error.localizedMessage ?: "Format berkas cadangan tidak valid."))
                }
            }
        )
    }

    fun dismissImportDialog() {
        _uiState.update {
            it.copy(
                pendingImportData = null,
                pendingImportSummary = null,
                isImportDialogVisible = false
            )
        }
    }

    fun confirmImport() {
        val data = _uiState.value.pendingImportData ?: return
        viewModelScope.launch {
            val result = repository.importBackup(data)
            dismissImportDialog()
            result.fold(
                onSuccess = {
                    _eventFlow.emit(SettingsEvent.ShowMessage("Data cadangan berhasil dipulihkan!"))
                },
                onFailure = { error ->
                    _eventFlow.emit(SettingsEvent.ShowMessage("Gagal memulihkan data: ${error.localizedMessage ?: "Terjadi kesalahan"}"))
                }
            )
        }
    }

    fun loadSampleData() {
        viewModelScope.launch {
            try {
                repository.loadSampleData()
                _eventFlow.emit(SettingsEvent.ShowMessage("Data contoh berhasil dimuat!"))
            } catch (e: Exception) {
                _eventFlow.emit(SettingsEvent.ShowMessage("Gagal memuat data contoh: ${e.localizedMessage}"))
            }
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            try {
                repository.clearAllData()
                _eventFlow.emit(SettingsEvent.ShowMessage("Seluruh data berhasil dibersihkan!"))
            } catch (e: Exception) {
                _eventFlow.emit(SettingsEvent.ShowMessage("Gagal menghapus data: ${e.localizedMessage}"))
            }
        }
    }
}
