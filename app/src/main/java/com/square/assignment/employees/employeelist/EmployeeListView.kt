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
import com.square.assignment.employees.data.model.Employee

import com.square.assignment.employees.employeelist.EmployeeListViewModel.UiState


@Composable
fun EmployeeListView(navController: NavController, viewModel: EmployeeListViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    val isRefreshing by remember { mutableStateOf(false) }
    val employees by viewModel.employeesFlow.collectAsState()

    Column() {

        when (uiState) {
            is UiState.Loading -> CircularProgressIndicator()
            is UiState.Success -> {
                if (employees.isNotEmpty()) {
                    EmployeeList(employees) { employeeId ->
                        navController.navigate("details/$employeeId")
                    }
                } else {
                    Text(text = "No employees found.")
                }
            }
            is UiState.Error -> Text(text = (uiState as UiState.Error).message)
            is UiState.Empty -> Text("No employees found.")
        }
    }
}


@Composable
fun EmployeeList(employees: List<Employee>, onItemClick: (String) -> Unit) {
    LazyColumn {
        items(employees) { employee ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(employee.uuid) }
                    .padding(16.dp)
            ) {
                androidx.compose.foundation.Image(
                    painter = rememberAsyncImagePainter(employee.photo_url_small),
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .padding(end = 8.dp)
                )
                Column {
                    Text(
                        text = employee.full_name,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = employee.team,
                        style = androidx.compose.ui.text.TextStyle(color = androidx.compose.ui.graphics.Color.Gray)
                    )
                }
            }
            androidx.compose.material3.Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = androidx.compose.ui.graphics.Color.LightGray
            )
        }
    }
}
