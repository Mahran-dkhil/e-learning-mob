package com.yourpackage.elearning.data.repository

import com.yourpackage.elearning.data.api.ApiService
import com.yourpackage.elearning.data.api.NetworkClient

class CategoryRepository(
    private val apiService: ApiService = NetworkClient.apiService
) {
    suspend fun getCategories() = apiService.getCategories()

    suspend fun getCoursesByCategory(categoryId: Int) =
        apiService.getCoursesByCategory(categoryId)
}
