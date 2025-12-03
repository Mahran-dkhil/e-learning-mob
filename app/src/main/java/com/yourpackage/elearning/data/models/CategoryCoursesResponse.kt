// data/models/CategoryCoursesResponse.kt
package com.yourpackage.elearning.data.models

import com.google.gson.annotations.SerializedName

data class CategoryCoursesResponse(
    @SerializedName("courses")
    val courses: List<Course> = emptyList(),

    @SerializedName("category")
    val category: Category? = null
)