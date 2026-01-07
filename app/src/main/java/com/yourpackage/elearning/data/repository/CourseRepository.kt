package com.yourpackage.elearning.data.repository

import com.yourpackage.elearning.data.api.ApiService
import com.yourpackage.elearning.data.api.NetworkClient

class CourseRepository(
    private val apiService: ApiService = NetworkClient.apiService
) {
    suspend fun getCourses() = apiService.getCourses()

    suspend fun getSubCoursesByCourse(courseId: Int) = apiService.getSubCoursesByCourse(courseId)

    suspend fun getCourseById(id: Int) = apiService.getCourseById(id)


    suspend fun getSubCourse(subCourseId: Int) = apiService.getSubCourse(subCourseId)
}
