package com.example.codingchallange.api

import com.example.codingchallange.details.model.SchoolDetails
import com.example.codingchallange.main.model.Schools
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiService {

    @GET(Constants.Schools)
    suspend fun getAllSchools(): Schools

    @GET(Constants.SchoolDetails)
    suspend fun getSchoolDetails(): SchoolDetails


}