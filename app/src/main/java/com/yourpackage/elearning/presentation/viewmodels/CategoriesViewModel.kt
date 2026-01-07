// presentation/viewmodels/CategoriesViewModel.kt
package com.yourpackage.elearning.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourpackage.elearning.data.models.Category
import com.yourpackage.elearning.data.models.Course
import com.yourpackage.elearning.data.repository.CategoryRepository
import kotlinx.coroutines.launch

class CategoriesViewModel(
    private val categoryRepository: CategoryRepository = CategoryRepository()
) : ViewModel() {

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    private val _categoryCourses = MutableLiveData<List<Course>>()
    val categoryCourses: LiveData<List<Course>> = _categoryCourses

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadCategories() {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val response = categoryRepository.getCategories()

                if (response.isSuccessful && response.body()?.success == true) {
                    _categories.value = response.body()?.data ?: emptyList()
                } else {
                    _error.value = "Failed to load categories"
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadCoursesByCategory(categoryId: Int) {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val response = categoryRepository.getCoursesByCategory(categoryId)

                if (response.isSuccessful && response.body()?.success == true) {
                    val courses = response.body()?.data?.courses ?: emptyList()
                    _categoryCourses.value = courses
                    _error.value = null
                } else {
                    _categoryCourses.value = emptyList()
                    _error.value = "Failed to load courses: ${response.message()}"
                }
            } catch (e: Exception) {
                _categoryCourses.value = emptyList()
                _error.value = "Network error: ${e.message}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
