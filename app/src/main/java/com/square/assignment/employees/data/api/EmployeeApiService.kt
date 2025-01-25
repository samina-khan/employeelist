package com.square.assignment.employees.data.api

import com.square.assignment.employees.data.model.EmployeeResponse
import retrofit2.http.GET

interface EmployeeApiService {
    @GET("employees_malformed.json")
    suspend fun getEmployees(): EmployeeResponse

}
