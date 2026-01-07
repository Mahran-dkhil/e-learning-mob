// presentation/fragments/CategoryCoursesFragment.kt
package com.yourpackage.elearning.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.yourpackage.elearning.R
import com.yourpackage.elearning.databinding.FragmentCategoryCoursesBinding
import com.yourpackage.elearning.presentation.adapters.CoursesAdapter
import com.yourpackage.elearning.presentation.viewmodels.CategoriesViewModel
import kotlin.getValue

class CategoryCoursesFragment : Fragment() {

    private var _binding: FragmentCategoryCoursesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoriesViewModel by viewModels()
    private val args: CategoryCoursesFragmentArgs by navArgs()
    private lateinit var coursesAdapter: CoursesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryCoursesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val category = args.category
        binding.tvTitle.text = getString(R.string.category_courses_title, category.name)
        binding.tvEmptyMessage.text = getString(R.string.category_courses_empty)

        setupRecyclerView()
        setupObservers()
        setupClickListeners()

        viewModel.loadCoursesByCategory(category.id)
    }

    private fun setupRecyclerView() {
        coursesAdapter = CoursesAdapter { course ->
            // Navigate to course details
            val action =
                CategoryCoursesFragmentDirections.actionCategoryCoursesFragmentToCourseDetailsFragment(course.id)
            findNavController().navigate(action)
        }

        binding.rvCourses.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = coursesAdapter
        }
    }

    private fun setupObservers() {
        viewModel.categoryCourses.observe(viewLifecycleOwner) { courses ->
            if (courses.isNullOrEmpty()) {
                showEmptyState()
            } else {
                binding.emptyLayout.visibility = View.GONE
                binding.errorLayout.visibility = View.GONE
                binding.rvCourses.visibility = View.VISIBLE
                coursesAdapter.submitList(courses)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) {
                binding.rvCourses.visibility = View.GONE
                binding.emptyLayout.visibility = View.GONE
                binding.errorLayout.visibility = View.GONE
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                binding.errorLayout.visibility = View.VISIBLE
                binding.tvError.text = error
                binding.rvCourses.visibility = View.GONE
                binding.emptyLayout.visibility = View.GONE
            }

            if (error == null && binding.progressBar.visibility == View.GONE) {
                binding.errorLayout.visibility = View.GONE
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnRetry.setOnClickListener {
            viewModel.loadCoursesByCategory(args.category.id)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showEmptyState() {
        if (binding.progressBar.visibility == View.VISIBLE) return
        coursesAdapter.submitList(emptyList())
        binding.rvCourses.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE
        binding.emptyLayout.visibility = View.VISIBLE
    }
}
