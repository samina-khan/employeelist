package com.square.assignment.employees.employeelist

import com.square.assignment.employees.data.model.Employee
import com.square.assignment.employees.data.model.EmployeeType
import com.square.assignment.employees.data.repository.EmployeesRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EmployeeListViewModelTest {

    private lateinit var viewModel: EmployeeListViewModel
    private val repository = mockk<EmployeesRepository>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = EmployeeListViewModel(repository, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchEmployees updates uiState to Success when data is returned`() = runTest {
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
                employee_type = EmployeeType.FULL_TIME
            )
        )

        coEvery { repository.getEmployeesFlow() } returns flowOf(mockEmployees)

        viewModel.fetchEmployees()
        advanceUntilIdle()

        assert(viewModel.uiState.value is EmployeeListViewModel.UiState.Success)
    }

    @Test
    fun `fetchEmployees updates uiState to Error when repository throws exception`() = runTest {
        val errorMessage = "Dummy Error"
        coEvery { repository.getEmployeesFlow() } returns flow {
            throw Exception(errorMessage)
        }

        viewModel.fetchEmployees()
        advanceUntilIdle()

        assert(viewModel.uiState.value is EmployeeListViewModel.UiState.Error)
        val errorState = viewModel.uiState.value as EmployeeListViewModel.UiState.Error
        assert(errorState.message.contains("Failed to load employee list: $errorMessage"))
    }

    @Test
    fun `fetchEmployees updates uiState to Empty when no employees are returned`() = runTest {
        coEvery { repository.getEmployeesFlow() } returns flowOf(emptyList())

        viewModel.fetchEmployees()
        advanceUntilIdle()

        assert(viewModel.uiState.value is EmployeeListViewModel.UiState.Empty)
    }
}

