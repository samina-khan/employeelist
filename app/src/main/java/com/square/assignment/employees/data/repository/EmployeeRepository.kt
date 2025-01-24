package com.square.assignment.employees.data.repository

import com.square.assignment.employees.data.api.EmployeeApiService
import com.square.assignment.employees.data.model.Employee
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EmployeesRepository @Inject constructor(
    private val api: EmployeeApiService
) {
    fun getEmployeesFlow(): Flow<List<Employee>> {
        return flow {
            val response = api.getEmployees()
            emit(response.employees)
        }
    }

    suspend fun getEmployeeDetails(uuid: String): Employee? {
        return api.getEmployees().employees.find { it.uuid == uuid }
    }
}

