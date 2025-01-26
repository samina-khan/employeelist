package com.square.assignment.employees.data.repository

import android.util.Log
import com.google.gson.JsonSyntaxException
import com.square.assignment.employees.data.api.EmployeeApiService
import com.square.assignment.employees.data.model.Employee
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EmployeesRepository @Inject constructor(
    private val api: EmployeeApiService
) {
    fun getEmployeesFlow(): Flow<List<Employee>> = flow {
        try {
            val response = api.getEmployees()
            emit(response.employees)
        } catch (e: Exception) {
            emit(emptyList())
            Log.d("EmployeesRepository", "Failed to load employees: ${e.message}")
            throw Exception("Server Error: ${e.message}")
        }
    }

    suspend fun getEmployeeDetails(uuid: String): Employee? {
        return api.getEmployees().employees.find { it.uuid == uuid }
    }
}

