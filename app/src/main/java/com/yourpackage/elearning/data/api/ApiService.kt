// data/api/ApiService.kt
package com.yourpackage.elearning.data.api

import com.yourpackage.elearning.data.models.ApiResponse
import com.yourpackage.elearning.data.models.Course
import com.yourpackage.elearning.data.models.Category
import com.yourpackage.elearning.data.models.CategoryCoursesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("courses")
    suspend fun getCourses(): Response<ApiResponse<List<Course>>>

    @GET("courses/{id}")
    suspend fun getCourse(@Path("id") courseId: Int): Response<ApiResponse<Course>>

    @GET("categories")
    suspend fun getCategories(): Response<ApiResponse<List<Category>>>

    @GET("categories/{id}/courses")
    suspend fun getCoursesByCategory(@Path("id") categoryId: Int): Response<ApiResponse<CategoryCoursesResponse>>
}