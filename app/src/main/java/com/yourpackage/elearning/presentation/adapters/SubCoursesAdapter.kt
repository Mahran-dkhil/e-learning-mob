package com.yourpackage.elearning.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yourpackage.elearning.R
import com.yourpackage.elearning.data.models.SubCourse
import com.yourpackage.elearning.databinding.ItemSubcourseBinding

class SubCoursesAdapter(
    private val onSubCourseClick: (SubCourse) -> Unit
) : ListAdapter<SubCourse, SubCoursesAdapter.SubCourseViewHolder>(SubCourseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCourseViewHolder {
        val binding = ItemSubcourseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SubCourseViewHolder(binding, onSubCourseClick)
    }

    override fun onBindViewHolder(holder: SubCourseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SubCourseViewHolder(
        private val binding: ItemSubcourseBinding,
        private val onSubCourseClick: (SubCourse) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(subCourse: SubCourse) {
            binding.tvSubcourseTitle.text = subCourse.title
            binding.tvSubcourseDuration.text = subCourse.duration?.let {
                binding.root.context.getString(R.string.duration_minutes, it)
            } ?: binding.root.context.getString(R.string.not_available)

            binding.root.setOnClickListener {
                onSubCourseClick(subCourse)
            }
        }
    }

    class SubCourseDiffCallback : DiffUtil.ItemCallback<SubCourse>() {
        override fun areItemsTheSame(oldItem: SubCourse, newItem: SubCourse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SubCourse, newItem: SubCourse): Boolean {
            return oldItem == newItem
        }
    }
}
