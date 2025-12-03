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
import com.yourpackage.elearning.databinding.FragmentCoursesBinding
import com.yourpackage.elearning.presentation.adapters.CoursesAdapter
import com.yourpackage.elearning.presentation.viewmodels.CoursesViewModel

class CoursesFragment : Fragment() {

    private var _binding: FragmentCoursesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CoursesViewModel by viewModels()
    private lateinit var coursesAdapter: CoursesAdapter

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
        viewModel.courses.observe(viewLifecycleOwner) { courses ->
            coursesAdapter.submitList(courses)
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

    private fun openCourseDetails(course: com.yourpackage.elearning.data.models.Course) {
        // Create bundle with course data
        val bundle = Bundle()
        bundle.putParcelable("course", course)

        // Navigate using Navigation Component
        findNavController().navigate(
            R.id.action_nav_courses_to_course_details_fragment,
            bundle
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}