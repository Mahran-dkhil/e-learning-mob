// presentation/adapters/CategoriesAdapter.kt
package com.yourpackage.elearning.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yourpackage.elearning.data.models.Category
import com.yourpackage.elearning.databinding.ItemCategoryBinding

class CategoriesAdapter(
    private val onCategoryClick: (Category) -> Unit
) : ListAdapter<Category, CategoriesAdapter.CategoryViewHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding, onCategoryClick)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = getItem(position)
        holder.bind(category)
    }

    inner class CategoryViewHolder(
        private val binding: ItemCategoryBinding,
        private val onCategoryClick: (Category) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.tvCategoryName.text = category.name
            binding.tvCoursesCount.visibility = View.GONE

            // Set icon based on category name or use default
            binding.tvCategoryIcon.text = getCategoryIcon(category.name)

            // Set background color if available
            category.color?.let { color ->
                try {
                    binding.root.setCardBackgroundColor(android.graphics.Color.parseColor(color))
                } catch (e: Exception) {
                    // Use default color if parsing fails
                }
            }

            // Set click listener
            binding.root.setOnClickListener {
                onCategoryClick(category)
            }
        }

        private fun getCategoryIcon(categoryName: String): String {
            return when (categoryName.lowercase()) {
                "programming", "coding" -> "💻"
                "design", "art" -> "🎨"
                "business", "marketing" -> "💼"
                "science", "math" -> "🔬"
                "language" -> "🌐"
                "music" -> "🎵"
                else -> "📚"
            }
        }
    }

    class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }
}