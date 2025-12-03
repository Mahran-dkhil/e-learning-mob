package com.yourpackage.elearning.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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
        val course = getItem(position)
        holder.bind(course)
    }

    inner class CourseViewHolder(
        private val binding: ItemCourseBinding,
        private val onCourseClick: (Course) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(course: Course) {
            binding.tvCourseTitle.text = course.title
            binding.tvCourseDescription.text = course.description ?: "No description"
            binding.tvDuration.text = "${course.duration} min"
            binding.tvXpPoints.text = "${course.xpPoints} XP"

            course.category?.let { category ->
                binding.tvCategory.text = category.name
                binding.tvCategory.visibility = View.VISIBLE
            } ?: run {
                binding.tvCategory.visibility = View.GONE
            }

            // Watch button click
            binding.btnWatch.setOnClickListener {
                onCourseClick(course)
            }

            // Whole card click (optional)
            binding.root.setOnClickListener {
                onCourseClick(course)
            }
        }
    }

    class CourseDiffCallback : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem == newItem
        }
    }
}