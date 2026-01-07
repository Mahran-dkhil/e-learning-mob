// presentation/fragments/CategoriesFragment.kt
package com.yourpackage.elearning.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.yourpackage.elearning.R
import com.yourpackage.elearning.databinding.FragmentCategoriesBinding
import com.yourpackage.elearning.presentation.adapters.CategoriesAdapter
import com.yourpackage.elearning.presentation.viewmodels.CategoriesViewModel

class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoriesViewModel by viewModels()
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupClickListeners()

        viewModel.loadCategories()
    }

    private fun setupRecyclerView() {
        categoriesAdapter = CategoriesAdapter { category ->
            // Navigate to category courses
            navigateToCategoryCourses(category)
        }

        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = categoriesAdapter
        }
    }

    private fun setupObservers() {
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            if (categories.isEmpty()) {
                binding.emptyLayout.visibility = View.VISIBLE
                binding.rvCategories.visibility = View.GONE
            } else {
                binding.emptyLayout.visibility = View.GONE
                binding.rvCategories.visibility = View.VISIBLE
                categoriesAdapter.submitList(categories)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.rvCategories.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                binding.errorLayout.visibility = View.VISIBLE
                binding.tvError.text = error
                binding.rvCategories.visibility = View.GONE
            } else {
                binding.errorLayout.visibility = View.GONE
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnRetry.setOnClickListener {
            viewModel.loadCategories()
        }
    }

    private fun navigateToCategoryCourses(category: com.yourpackage.elearning.data.models.Category) {
        val action =
            CategoriesFragmentDirections.actionNavCategoriesToCategoryCoursesFragment(category)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
