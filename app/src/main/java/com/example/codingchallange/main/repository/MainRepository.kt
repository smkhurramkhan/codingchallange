package com.example.codingchallange.main.repository

import com.example.codingchallange.main.model.model.SchoolDetails
import com.example.codingchallange.main.model.Schools

interface MainRepository {
    suspend fun getAllSchools(): Schools

    suspend fun getSchoolDetails(): SchoolDetails
}