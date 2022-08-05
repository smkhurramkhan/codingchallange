package com.example.codingchallange.main.repository

import com.example.codingchallange.api.ApiService
import com.example.codingchallange.details.model.SchoolDetails
import com.example.codingchallange.main.model.Schools
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : MainRepository {

    override suspend fun getAllSchools(): Schools {
        return apiService.getAllSchools()
    }

    override suspend fun getSchoolDetails(): SchoolDetails {
        return apiService.getSchoolDetails()
    }
}