package com.square.assignment.employees.employeedetails

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
class EmployeeDetailsViewModel @Inject constructor(
    private val repository: EmployeesRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> get() = _uiState

    fun fetchEmployeeDetails(id: String) {
        viewModelScope.launch(dispatcher) {
            try {
                val employee = repository.getEmployeeDetails(id)
                if(employee == null) {
                    _uiState.value = UiState.Error("Employee not found")
                } else {
                    _uiState.value =  UiState.Success(employee)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to load details: ${e.message}")
            }
        }
    }

    sealed class UiState {
        object Loading : UiState()
        data class Success(val employee: Employee) : UiState()
        data class Error(val message: String) : UiState()
    }
}
