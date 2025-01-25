package com.square.assignment.employees.data.repository

import com.square.assignment.employees.data.api.EmployeeApiService
import com.square.assignment.employees.data.model.Employee
import com.square.assignment.employees.data.model.EmployeeResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EmployeesRepositoryTest {

    private lateinit var repository: EmployeesRepository
    private val api = mockk<EmployeeApiService>()

    @Before
    fun setUp() {
        repository = EmployeesRepository(api)
    }

    @Test
    fun `getEmployeesFlow emits a list of employees`() = runTest {
        val mockEmployees = listOf(
            Employee(
                uuid = "1234",
                full_name = "John Doe",
                phone_number = "5551234567",
                email_address = "jdoe@example.com",
                biography = "Software Engineer",
                photo_url_small = "https://example.com/small.jpg",
                photo_url_large = "https://example.com/large.jpg",
                team = "Engineering",
                employee_type = "FULL_TIME"
            )
        )
        val mockResponse = EmployeeResponse(employees = mockEmployees)

        coEvery { api.getEmployees() } returns mockResponse

        val flow = repository.getEmployeesFlow()
        val result = mutableListOf<List<Employee>>()
        flow.collect { result.add(it) }

        assert(result.size == 1)
        assert(result.first() == mockEmployees)
    }

    @Test
    fun `getEmployeesFlow emits an empty list when API returns no employees`() = runTest {
        val mockResponse = EmployeeResponse(employees = emptyList())

        coEvery { api.getEmployees() } returns mockResponse

        val flow = repository.getEmployeesFlow()
        val result = mutableListOf<List<Employee>>()
        flow.collect { result.add(it) }

        assert(result.size == 1)
        assert(result.first().isEmpty())
    }

    @Test
    fun `getEmployeeDetails returns details of an employee`() = runTest {
        val mockEmployees = listOf(
            Employee(
                uuid = "1234",
                full_name = "John Doe",
                phone_number = "5551234567",
                email_address = "jdoe@example.com",
                biography = "Software Engineer",
                photo_url_small = "https://example.com/small.jpg",
                photo_url_large = "https://example.com/large.jpg",
                team = "Engineering",
                employee_type = "FULL_TIME"
            )
        )
        val mockResponse = EmployeeResponse(employees = mockEmployees)

        coEvery { api.getEmployees() } returns mockResponse

        val result = repository.getEmployeeDetails("1234")

        assert(result == mockEmployees.first())
    }

    @Test
    fun `getEmployeeDetails returns null if employee is not found`() = runTest {
        val mockResponse = EmployeeResponse(employees = emptyList())

        coEvery { api.getEmployees() } returns mockResponse

        val result = repository.getEmployeeDetails("unknown")

        assert(result == null)
    }
}