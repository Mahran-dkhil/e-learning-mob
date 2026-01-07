package com.yourpackage.elearning.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubCourseLight(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("orderNumber")
    val orderNumber: Int? = null
) : Parcelable

@Parcelize
data class SubCourse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("duration")
    val duration: Int? = null,

    // API returns "videoUrl"
    @SerializedName("videoUrl")
    val videoUrl: String? = null,

    // API returns "orderNumber"
    @SerializedName("orderNumber")
    val orderNumber: Int? = null
) : Parcelable
