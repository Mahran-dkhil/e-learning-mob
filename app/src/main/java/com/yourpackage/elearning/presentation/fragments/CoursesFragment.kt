package com.yourpackage.elearning.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yourpackage.elearning.R
import com.yourpackage.elearning.data.models.Course
import com.yourpackage.elearning.databinding.FragmentCoursesBinding
import com.yourpackage.elearning.presentation.adapters.CoursesAdapter
import com.yourpackage.elearning.presentation.viewmodels.CoursesViewModel
import androidx.appcompat.widget.SearchView

class CoursesFragment : Fragment() {

    private var _binding: FragmentCoursesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CoursesViewModel by viewModels()
    private lateinit var coursesAdapter: CoursesAdapter

    private var allCourses: List<Course> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoursesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupClickListeners()
        setupSearch()
        binding.svCourses.apply {
            isIconified=false
            setIconifiedByDefault(false)
            queryHint = "Search by course or category..."
            clearFocus()
        }

        viewModel.loadCourses()
    }

    private fun setupRecyclerView() {
        coursesAdapter = CoursesAdapter { course ->
            // Open Course Details using Navigation Component
            openCourseDetails(course)
        }

        binding.rvCourses.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = coursesAdapter
        }
    }

    private fun setupObservers() {
        viewModel.courses.observe(viewLifecycleOwner) {
            allCourses = it
            coursesAdapter.submitList(it)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.rvCourses.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                binding.errorLayout.visibility = View.VISIBLE
                binding.tvError.text = error
                binding.rvCourses.visibility = View.GONE
            } else {
                binding.errorLayout.visibility = View.GONE
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnRetry.setOnClickListener {
            viewModel.loadCourses()
        }
    }

    private fun openCourseDetails(course: Course) {
        val action = CoursesFragmentDirections
            .actionNavCoursesToCourseDetailsFragment(course.id)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupSearch() {
        binding.svCourses.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true

            override fun onQueryTextChange(newText: String?): Boolean {
                val q = newText.orEmpty().trim().lowercase()

                val filtered = if (q.isBlank()) {
                    allCourses
                } else {
                    allCourses.filter { c ->
                        val titleMatch = c.title.orEmpty().lowercase().contains(q)
                        val categoryMatch = c.category?.name.orEmpty().lowercase().contains(q)
                        titleMatch || categoryMatch
                    }
                }

                coursesAdapter.submitList(filtered)
                return true
            }
        })
    }
}