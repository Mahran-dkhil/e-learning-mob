package com.yourpackage.elearning.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Course(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("category")
    val category: Category?,

    // IMPORTANT: can be null in list responses or when API doesn't include it
    @SerializedName("subcourses")
    val subcourses: List<SubCourseLight>? = emptyList(),

    @SerializedName("is_published")
    val isPublished: Boolean? = true
) : Parcelable
