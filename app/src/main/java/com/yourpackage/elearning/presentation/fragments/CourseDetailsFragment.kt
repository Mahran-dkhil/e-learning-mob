package com.yourpackage.elearning.presentation.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.yourpackage.elearning.databinding.FragmentCourseDetailsBinding

class CourseDetailsFragment : Fragment() {

    private var _binding: FragmentCourseDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var course: com.yourpackage.elearning.data.models.Course
    private lateinit var youTubePlayerView: YouTubePlayerView
    private var videoId: String? = null

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

        // Get course from arguments
        arguments?.let { bundle ->
            course = bundle.getParcelable("course")!!
            setupUI()
            setupYouTubePlayer()
            setupClickListeners()
        } ?: run {
            // If no course found, go back
            findNavController().popBackStack()
        }
    }

    private fun setupUI() {
        // Set course data
        binding.tvCourseTitle.text = course.title
        binding.tvCourseDescription.text = course.description ?: "No description available"
        binding.tvDuration.text = "${course.duration} min"
        binding.tvXpPoints.text = "${course.xpPoints} XP"

        // Set category if available
        course.category?.let { category ->
            binding.tvCategory.text = category.name
            binding.tvCategory.visibility = View.VISIBLE
        } ?: run {
            binding.tvCategory.visibility = View.GONE
        }
    }

    private fun setupYouTubePlayer() {
        youTubePlayerView = binding.youtubePlayerView

        // Add lifecycle observer
        lifecycle.addObserver(youTubePlayerView)

        // Extract video ID
        videoId = extractYouTubeVideoId(course.videoUrl)

        // Simple initialization without options
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                if (videoId != null) {
                    youTubePlayer.loadVideo(videoId!!, 0f)
                }
            }

            override fun onError(youTubePlayer: YouTubePlayer, error: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerError) {
                // Show error overlay when there's an error
                binding.errorOverlay.visibility = View.VISIBLE
                binding.tvYoutubeError.text = "Error: $error\nTap to open in YouTube app"
            }
        })
    }

    private fun setupClickListeners() {
        // Mark as complete button
        binding.btnMarkComplete.setOnClickListener {
            binding.btnMarkComplete.text = "Completed ✓"
            binding.btnMarkComplete.isEnabled = false
        }

        // Click on error overlay to open YouTube app
        binding.errorOverlay.setOnClickListener {
            openVideoInYouTubeApp()
        }

        // Also make the whole video container clickable when there's an error
        binding.videoContainer.setOnClickListener {
            if (binding.errorOverlay.visibility == View.VISIBLE) {
                openVideoInYouTubeApp()
            }
        }
    }

    private fun openVideoInYouTubeApp() {
        videoId?.let { id ->
            try {
                // Try to open in YouTube app first
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("vnd.youtube:$id")
                intent.putExtra("VIDEO_ID", id)
                startActivity(intent)
            } catch (e: Exception) {
                // If YouTube app is not installed, open in browser
                val webIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/watch?v=$id")
                )
                startActivity(webIntent)
            }
        } ?: run {
            // If no video ID, open YouTube homepage
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.youtube.com")
            startActivity(intent)
        }
    }

    private fun extractYouTubeVideoId(url: String): String? {
        val regex = Regex("(?<=v=|v/|vi=|vi/|embed/|youtu.be/|watch\\?v=|watch\\?.&v=)([A-Za-z0-9_-]{11})")
        return regex.find(url)?.value
    }


    override fun onDestroyView() {
        super.onDestroyView()
        youTubePlayerView.release()
        _binding = null
    }
}