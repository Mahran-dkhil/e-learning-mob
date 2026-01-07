package com.yourpackage.elearning.presentation.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.yourpackage.elearning.R
import com.yourpackage.elearning.data.models.SubCourse
import com.yourpackage.elearning.databinding.FragmentSubcourseDetailsBinding

class SubCourseDetailsFragment : Fragment() {

    private var _binding: FragmentSubcourseDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var subCourse: SubCourse

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubcourseDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subCourse = arguments?.getParcelable("subcourse")
            ?: run {
                findNavController().popBackStack()
                return
            }

        bindUi()
        setupWatchButton()
    }

    private fun bindUi() {
        binding.tvSubcourseTitle.text = subCourse.title
        binding.tvSubcourseDuration.text = subCourse.duration?.let {
            getString(R.string.duration_minutes, it)
        } ?: getString(R.string.not_available)
    }

    private fun setupWatchButton() {
        binding.btnWatchYoutube.setOnClickListener {
            val videoUrl = subCourse.videoUrl
            if (videoUrl.isNullOrBlank()) {
                showNoVideoMessage()
                return@setOnClickListener
            }

            val videoId = extractYouTubeVideoId(videoUrl)
            val pm = requireContext().packageManager

            try {
                if (!videoId.isNullOrBlank()) {
                    val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
                    if (appIntent.resolveActivity(pm) != null) {
                        startActivity(appIntent)
                        return@setOnClickListener
                    }
                }

                val webUri = try {
                    if (!videoId.isNullOrBlank()) {
                        Uri.parse("https://www.youtube.com/watch?v=$videoId")
                    } else {
                        Uri.parse(videoUrl)
                    }
                } catch (e: Exception) {
                    null
                }

                if (webUri != null) {
                    startActivity(Intent(Intent.ACTION_VIEW, webUri))
                } else {
                    showNoVideoMessage()
                }
            } catch (e: Exception) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.video_open_error),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showNoVideoMessage() {
        Snackbar.make(
            binding.root,
            getString(R.string.no_video_available_message),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun extractYouTubeVideoId(url: String?): String? {
        if (url.isNullOrBlank()) return null

        val patterns = listOf(
            "youtu\\.be/([A-Za-z0-9_-]{11})",
            "youtube\\.com/watch\\?v=([A-Za-z0-9_-]{11})",
            "youtube\\.com/embed/([A-Za-z0-9_-]{11})",
            "youtube\\.com/shorts/([A-Za-z0-9_-]{11})",
            "[?&]v=([A-Za-z0-9_-]{11})"
        )

        for (p in patterns) {
            val match = Regex(p).find(url)
            if (match != null) return match.groupValues[1]
        }

        return null
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
