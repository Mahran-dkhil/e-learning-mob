package com.yourpackage.elearning.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yourpackage.elearning.R
import com.yourpackage.elearning.data.models.Course
import com.yourpackage.elearning.databinding.ItemCourseBinding

class CoursesAdapter(
    private val onCourseClick: (Course) -> Unit
) : ListAdapter<Course, CoursesAdapter.CourseViewHolder>(CourseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemCourseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CourseViewHolder(binding, onCourseClick)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CourseViewHolder(
        private val binding: ItemCourseBinding,
        private val onCourseClick: (Course) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(course: Course) {
            binding.tvCourseTitle.text = course.title

            binding.tvCourseDescription.text =
                course.description ?: binding.root.context.getString(
                    com.yourpackage.elearning.R.string.no_description_available
                )

            course.category?.let { category ->
                binding.tvCategory.text = category.name
                binding.tvCategory.visibility = View.VISIBLE
            } ?: run {
                binding.tvCategory.visibility = View.GONE
            }

            // IMPORTANT:
            // In list endpoints (categories/courses), API often doesn't return subcourses.
            // So we hide "lessons count" in lists to avoid showing misleading "0 lessons".
            val lessonsCount = course.subcourses?.size
            if (lessonsCount != null && lessonsCount > 0) {
                binding.tvDuration.visibility = View.VISIBLE
                binding.tvDuration.text = binding.root.context.getString(
                    R.string.lessons_count,
                    lessonsCount
                )
            } else {
                binding.tvDuration.visibility = View.GONE
            }


            binding.btnWatch.setOnClickListener { onCourseClick(course) }
            binding.root.setOnClickListener { onCourseClick(course) }
        }
    }

    class CourseDiffCallback : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean = oldItem == newItem
    }
}
