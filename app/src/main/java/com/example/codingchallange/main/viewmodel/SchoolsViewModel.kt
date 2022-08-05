package com.example.codingchallange.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.codingchallange.main.repository.MainRepositoryImpl
import com.example.codingchallange.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class SchoolsViewModel @Inject constructor(
    private val mainRepositoryImpl: MainRepositoryImpl,

    ) : ViewModel() {

    fun getAllSchools() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(data = mainRepositoryImpl.getAllSchools()))
        } catch (exception: Exception) {
            emit(Resource.error(exception.message ?: "Error Occurred!", data = null))
        }
    }

    fun getSchoolDetails() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(data = mainRepositoryImpl.getSchoolDetails()))
        } catch (exception: Exception) {
            emit(Resource.error(exception.message ?: "Error Occurred!", data = null))
        }
    }
}