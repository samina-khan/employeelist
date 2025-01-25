package com.square.assignment.employees.data.api


import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EmployeeApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: EmployeeApiService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EmployeeApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getEmployees makes correct API call and parses response`() = runBlocking {
        val mockResponse = """{
            "employees": [
                {
                    "uuid": "1234",
                    "full_name": "John Doe",
                    "phone_number": "5551234567",
                    "email_address": "jdoe@example.com",
                    "biography": "Software Engineer",
                    "photo_url_small": "https://example.com/small.jpg",
                    "photo_url_large": "https://example.com/large.jpg",
                    "team": "Engineering",
                    "employee_type": "FULL_TIME"
                }
            ]
        }"""
        mockWebServer.enqueue(MockResponse().setBody(mockResponse).setResponseCode(200))


        val response = api.getEmployees()


        assert(response.employees.size == 1)
        assert(response.employees[0].full_name == "John Doe")
        assert(response.employees[0].team == "Engineering")
    }
}