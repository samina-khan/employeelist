package com.square.assignment.employees.employeedetails

import com.square.assignment.employees.data.model.Employee
import com.square.assignment.employees.data.model.EmployeeType
import com.square.assignment.employees.data.repository.EmployeesRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EmployeeDetailsViewModelTest {

    private lateinit var viewModel: EmployeeDetailsViewModel
    private val repository = mockk<EmployeesRepository>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = EmployeeDetailsViewModel(repository, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchEmployeeDetails updates uiState to Success when data is returned`() = runTest {
        val mockEmployee = Employee(
            uuid = "1234",
            full_name = "John Doe",
            phone_number = "5551234567",
            email_address = "jdoe@example.com",
            biography = "Software Engineer",
            photo_url_small = "https://example.com/small.jpg",
            photo_url_large = "https://example.com/large.jpg",
            team = "Engineering",
            employee_type = EmployeeType.FULL_TIME
        )

        coEvery { repository.getEmployeeDetails("1234") } returns mockEmployee

        viewModel.fetchEmployeeDetails("1234")
        advanceUntilIdle()

        assert(viewModel.uiState.value is EmployeeDetailsViewModel.UiState.Success)
        val successState = viewModel.uiState.value as EmployeeDetailsViewModel.UiState.Success
        assert(successState.employee == mockEmployee)
    }

    @Test
    fun `fetchEmployeeDetails updates uiState to Error when employee is not found`() = runTest {
        coEvery { repository.getEmployeeDetails("9999") } returns null

        viewModel.fetchEmployeeDetails("9999")
        advanceUntilIdle()

        assert(viewModel.uiState.value is EmployeeDetailsViewModel.UiState.Error)
        val errorState = viewModel.uiState.value as EmployeeDetailsViewModel.UiState.Error
        assert(errorState.message.contains("Employee not found"))
    }

    @Test
    fun `fetchEmployeeDetails updates uiState to Error when repository throws exception`() = runTest {
        coEvery { repository.getEmployeeDetails("1234") } throws Exception("Network error")

        viewModel.fetchEmployeeDetails("1234")
        advanceUntilIdle()

        assert(viewModel.uiState.value is EmployeeDetailsViewModel.UiState.Error)
        val errorState = viewModel.uiState.value as EmployeeDetailsViewModel.UiState.Error
        assert(errorState.message.contains("Failed to load details: Network error"))
    }
}
