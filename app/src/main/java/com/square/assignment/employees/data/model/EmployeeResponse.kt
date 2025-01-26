package com.square.assignment.employees.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmployeeResponse(
    val employees: List<Employee> = emptyList()
)

@Serializable
enum class EmployeeType {
    @SerialName("FULL_TIME") FULL_TIME,
    @SerialName("PART_TIME") PART_TIME,
    @SerialName("CONTRACTOR") CONTRACTOR
}

@Serializable
data class Employee(
    val uuid: String,
    val full_name: String,
    val phone_number: String? = null,
    val email_address: String,
    val biography: String? = null,
    val photo_url_small: String? = null,
    val photo_url_large: String? = null,
    val team: String,
    val employee_type: EmployeeType
)
{
    init {
        require(uuid.isNotEmpty()) { "uuid cannot be empty" }
        require(full_name.isNotEmpty()) { "full_name cannot be empty" }
        require(email_address.isNotEmpty()) { "email_address cannot be empty" }
        require(team.isNotEmpty()) { "team cannot be empty" }
    }
}