package com.example.storyappfrans.ui.main

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.storyappfrans.R
import com.example.storyappfrans.data.local.entity.Story
import com.example.storyappfrans.utils.ViewModelFactory
import com.example.storyappfrans.data.repository.Result
import com.example.storyappfrans.databinding.ActivityStoryDetailBinding

class StoryDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityStoryDetailBinding
    private lateinit var nameTextView: TextView
    private lateinit var photoImageView: ImageView
    private lateinit var descriptionTextView: TextView
    private lateinit var apiViewModel: ApiViewModel
    private lateinit var loadingProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nameTextView = binding.tvDetailName
        photoImageView = binding.ivDetailPhoto
        descriptionTextView = binding.tvDetailDescription
        loadingProgressBar = binding.progressBarLoading

        val viewModelFactory = ViewModelFactory.getInstance()
        apiViewModel = ViewModelProvider(this, viewModelFactory).get(ApiViewModel::class.java)

        val story = intent.getParcelableExtra<Story>("story")

        getStoryDetail(story?.id ?: "")
    }

    private fun getStoryDetail(storyId: String) {
        val token = getToken()
        val authorization = getString(R.string.bearer) + " " + (token ?: "")

        apiViewModel.getDetailStory(authorization, storyId).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    loadingProgressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    val story = result.data
                    story?.let {
                        nameTextView.text = it.userName
                        Glide.with(this)
                            .load(story.photoUrl)
                            .circleCrop()
                            .into(photoImageView)
                        descriptionTextView.text = it.description
                        loadingProgressBar.visibility = View.GONE
                    }
                }
                is Result.Error -> {
                    loadingProgressBar.visibility = View.GONE
                    Toast.makeText(this, "Failed to fetch story details", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getToken(): String? {
        val sharedPreferences =
            getSharedPreferences(getString(R.string.my_preferences), Context.MODE_PRIVATE)
        return sharedPreferences.getString(getString(R.string.key_token), null)
    }
}

