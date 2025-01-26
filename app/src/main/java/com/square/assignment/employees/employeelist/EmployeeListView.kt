package com.square.assignment.employees.employeelist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.square.assignment.employees.data.model.Employee

import com.square.assignment.employees.employeelist.EmployeeListViewModel.UiState

@Composable
fun EmployeeListView(navController: NavController, viewModel: EmployeeListViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val employees by viewModel.employeesFlow.collectAsState()

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { viewModel.fetchEmployees() }
    ) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            when (uiState) {
                is UiState.Loading -> item {

                }
                is UiState.Error -> item {
                    Text(text = (uiState as UiState.Error).message, modifier = Modifier.padding(16.dp))
                }
                is UiState.Empty -> item {
                    Text(text = "No employees found.", modifier = Modifier.padding(16.dp))
                }
                is UiState.Success -> {
                    if (employees.isNotEmpty()) {
                        items(employees) { employee ->
                            if (!employee.uuid.isNullOrEmpty()) {
                                EmployeeItem(employee) { employeeId ->
                                    navController.navigate("details/$employeeId")
                                }
                            }
                        }
                    } else {
                        item {
                            Text(text = "No employees found.", modifier = Modifier.padding(16.dp))
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun EmployeeItem(employee: Employee, onItemClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { employee.uuid.let { onItemClick(it) } }
            .padding(16.dp)
    ) {
        androidx.compose.foundation.Image(
            painter = rememberAsyncImagePainter(employee.photo_url_small ?: ""),
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .padding(end = 8.dp)
        )
        Column {
            Text(
                text = employee.full_name.ifEmpty { "Unknown Name" },
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = employee.team.ifEmpty { "Unknown Team" },
                style = androidx.compose.ui.text.TextStyle(color = androidx.compose.ui.graphics.Color.Gray)
            )
        }
    }
    androidx.compose.material3.Divider(
        modifier = Modifier.padding(vertical = 8.dp),
        color = androidx.compose.ui.graphics.Color.LightGray
    )
}
