package com.example.codingchallange.main.repository

import com.example.codingchallange.details.model.SchoolDetails
import com.example.codingchallange.main.model.Schools

interface MainRepository {
    suspend fun getAllSchools(): Schools

    suspend fun getSchoolDetails(): SchoolDetails
}