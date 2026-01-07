package com.yourpackage.elearning.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.yourpackage.elearning.R
import com.yourpackage.elearning.data.models.SubCourse
import com.yourpackage.elearning.data.repository.CourseRepository
import com.yourpackage.elearning.databinding.FragmentCourseDetailsBinding
import com.yourpackage.elearning.presentation.adapters.SubCoursesAdapter
import kotlinx.coroutines.launch

class CourseDetailsFragment : Fragment() {

    private var _binding: FragmentCourseDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: CourseDetailsFragmentArgs by navArgs()
    private val courseRepository = CourseRepository()
    private lateinit var subCoursesAdapter: SubCoursesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSubCoursesList()

        // We only have courseId now
        val courseId = args.courseId

        // Optional: show placeholders until you add "getCourseById"
        binding.tvCourseTitle.text = ""
        binding.tvCourseDescription.text = ""
        binding.tvCategory.visibility = View.GONE
        loadCourseDetails(courseId)
        loadSubCourses(courseId)
    }

    private fun setupSubCoursesList() {
        subCoursesAdapter = SubCoursesAdapter { subCourse ->
            openSubCourseDetails(subCourse)
        }

        binding.rvSubcourses.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = subCoursesAdapter
        }
    }

    private fun loadSubCourses(courseId: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = courseRepository.getSubCoursesByCourse(courseId)
                if (response.isSuccessful && response.body()?.success == true) {
                    val subCourses = response.body()?.data.orEmpty()
                    subCoursesAdapter.submitList(subCourses)
                    toggleEmptyState(subCourses)
                } else {
                    showEmptyState(getString(R.string.error_loading_lessons))
                }
            } catch (e: Exception) {
                showEmptyState(getString(R.string.error_loading_lessons))
            }
        }
    }

    private fun loadCourseDetails(courseId: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = courseRepository.getCourseById(courseId)
                if (response.isSuccessful && response.body()?.success == true) {
                    val course = response.body()!!.data

                    binding.tvCourseTitle.text = course?.title
                    binding.tvCourseDescription.text =
                        course?.description ?: getString(R.string.no_description_available)

                    val categoryName = course?.category?.name
                    if (categoryName.isNullOrBlank()) {
                        binding.tvCategory.visibility = View.GONE
                    } else {
                        binding.tvCategory.visibility = View.VISIBLE
                        binding.tvCategory.text = categoryName
                    }
                } else {
                    // Don't break the screen; just show fallback text
                    binding.tvCourseTitle.text = ""
                    binding.tvCourseDescription.text = ""
                    binding.tvCategory.visibility = View.GONE
                }
            } catch (_: Exception) {
                binding.tvCourseTitle.text = ""
                binding.tvCourseDescription.text = ""
                binding.tvCategory.visibility = View.GONE
            }
        }
    }


    private fun toggleEmptyState(items: List<SubCourse>) {
        if (items.isEmpty()) {
            showEmptyState(getString(R.string.no_lessons_available))
        } else {
            binding.tvSubcoursesEmpty.visibility = View.GONE
            binding.rvSubcourses.visibility = View.VISIBLE
        }
    }

    private fun showEmptyState(message: String) {
        binding.tvSubcoursesEmpty.text = message
        binding.tvSubcoursesEmpty.visibility = View.VISIBLE
        binding.rvSubcourses.visibility = View.GONE
    }

    private fun openSubCourseDetails(subCourse: SubCourse) {
        // Use SafeArgs instead of manual bundle
        val action =
            CourseDetailsFragmentDirections.actionCourseDetailsFragmentToSubCourseDetailsFragment(subCourse)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
