package com.square.assignment.employees.employeedetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.square.assignment.employees.data.model.Employee

import com.square.assignment.employees.employeedetails.EmployeeDetailsViewModel.UiState


@Composable
fun EmployeeDetailsView(
    navController: NavController,
    id: String?,
    viewModel: EmployeeDetailsViewModel = hiltViewModel()
) {
    LaunchedEffect(id) {
        if (id != null) {
            viewModel.fetchEmployeeDetails(id)
        }
    }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
        }



        Spacer(modifier = Modifier.height(16.dp))

        val uiState by viewModel.uiState.collectAsState()

        when (uiState) {
            is UiState.Loading -> CircularProgressIndicator()
            is UiState.Success -> {
                val employee = (uiState as UiState.Success).employee
                EmployeeDetails(employee)
            }

            is UiState.Error -> Text(text = (uiState as UiState.Error).message)
        }
    }
}

@Composable
fun EmployeeDetails(employee: Employee) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            AsyncImage(
                model = employee.photo_url_large?:"",
                contentDescription = "Employee Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = employee.full_name?:"",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = employee.team?:"",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "email: ${employee.email_address?:""}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}