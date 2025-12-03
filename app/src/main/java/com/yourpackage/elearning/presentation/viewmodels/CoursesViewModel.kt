package com.yourpackage.elearning.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourpackage.elearning.data.api.NetworkClient
import com.yourpackage.elearning.data.models.Course
import kotlinx.coroutines.launch

class CoursesViewModel : ViewModel() {

    private val _courses = MutableLiveData<List<Course>>()
    val courses: LiveData<List<Course>> = _courses

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadCourses() {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val response = NetworkClient.apiService.getCourses()

                if (response.isSuccessful && response.body()?.success == true) {
                    _courses.value = response.body()?.data ?: emptyList()
                } else {
                    _error.value = "Failed to load courses"
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}