// data/models/Course.kt
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

    @SerializedName("video_url")
    val videoUrl: String,

    @SerializedName("duration")
    val duration: Int,

    @SerializedName("xp_points")
    val xpPoints: Int,

    @SerializedName("category")
    val category: Category?,

    @SerializedName("is_published")
    val isPublished: Boolean? = true
) : Parcelable
