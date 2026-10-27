package dev.example.permissioncrashdemo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.example.permissioncrashdemo.data.ContactCountResult
import dev.example.permissioncrashdemo.data.ContactsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DemoViewModel(private val repository: ContactsRepository) : ViewModel() {
    private val mutableUiState = MutableStateFlow(DemoUiState())
    val uiState = mutableUiState.asStateFlow()

    fun onPermissionGranted() {
        if (mutableUiState.value.status == DemoStatus.Loading) return
        mutableUiState.value = DemoUiState(DemoStatus.Loading)
        viewModelScope.launch {
            mutableUiState.value = when (val result = repository.countContacts()) {
                is ContactCountResult.Success -> DemoUiState(DemoStatus.Success, result.count)
                ContactCountResult.PermissionRequired -> DemoUiState(DemoStatus.PermissionRequired)
                ContactCountResult.Unavailable -> DemoUiState(DemoStatus.Unavailable)
            }
        }
    }

    fun onPermissionDenied(permanentlyDenied: Boolean) {
        mutableUiState.value = DemoUiState(
            if (permanentlyDenied) DemoStatus.Blocked else DemoStatus.Denied,
        )
    }
}
