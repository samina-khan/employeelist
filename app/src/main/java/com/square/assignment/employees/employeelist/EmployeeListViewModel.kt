package com.square.assignment.employees.employeelist

import android.util.Log
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


    init {
        fetchEmployees()
    }

    fun fetchEmployees() {
        viewModelScope.launch(dispatcher) {
            _uiState.value = UiState.Loading
            try {
                repository.getEmployeesFlow().collect { employees ->
                    _employeesFlow.value = employees
                    _uiState.value = if (employees.isEmpty()) UiState.Empty else UiState.Success
                    Log.d("EmployeeListViewModel", "Employees: $employees")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to load employee list: ${e.message}")
            }
        }
    }

    sealed class UiState {
        object Loading : UiState()
        object Success : UiState()
        data class Error(val message: String) : UiState()
        object Empty : UiState()
    }
}