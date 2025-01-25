package com.square.assignment.employees.employeelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.square.assignment.employees.data.model.Employee
import com.square.assignment.employees.data.repository.EmployeesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EmployeeListViewModel @Inject constructor(
    private val repository: EmployeesRepository,
    private val dispatcher: CoroutineDispatcher

) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> get() = _uiState

    private val _employeesFlow = MutableStateFlow<List<Employee>>(emptyList())
    val employeesFlow: StateFlow<List<Employee>> get() = _employeesFlow

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        fetchEmployees()
    }

    fun fetchEmployees() {
        viewModelScope.launch(dispatcher) {
            _isRefreshing.value = true
            _uiState.value = UiState.Loading
            try {
                repository.getEmployeesFlow().collect { employees ->
                    _employeesFlow.value = employees
                    _uiState.value = if (employees.isEmpty()) UiState.Empty else UiState.Success
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to load employee list: ${e.message}")
            }
            _isRefreshing.value = false
        }
    }

    sealed class UiState {
        object Loading : UiState()
        object Success : UiState()
        data class Error(val message: String) : UiState()
        object Empty : UiState()
    }
}