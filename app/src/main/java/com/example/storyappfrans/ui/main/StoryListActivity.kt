package com.example.storyappfrans.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storyappfrans.R
import com.example.storyappfrans.databinding.ActivityStoryListBinding
import com.example.storyappfrans.utils.ViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class StoryListActivity : BaseActivity() {
    private lateinit var binding: ActivityStoryListBinding
    private lateinit var storyRecyclerView: RecyclerView
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var errorTextView: TextView
    private lateinit var noDataTextView: TextView
    private lateinit var addStoryFloatingButton: FloatingActionButton
    private lateinit var mapsButton: FloatingActionButton
    private lateinit var adapter: StoryAdapter
    private lateinit var apiViewModel: ApiViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyRecyclerView = binding.recyclerViewStory
        loadingProgressBar = binding.progressBarLoading
        errorTextView = binding.textViewError
        noDataTextView = binding.textViewNoData
        addStoryFloatingButton = binding.fabAddStory
        mapsButton = binding.fabMaps

        val viewModelFactory = ViewModelFactory.getInstance()
        apiViewModel = ViewModelProvider(this, viewModelFactory)[ApiViewModel::class.java]

        val layoutManager = LinearLayoutManager(this)
        adapter = StoryAdapter()
        storyRecyclerView.layoutManager = layoutManager
        storyRecyclerView.adapter = adapter

        fetchStoryList()

        addStoryFloatingButton.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }

        mapsButton.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchStoryList() {
        val token = getToken()
        apiViewModel.getAllStories(
            getString(R.string.bearer) + " " + (token ?: ""),
            0
        ).observe(this) {
            Log.d("PagingData", it.toString())
            this.adapter.submitData(lifecycle, it)
        }
        println("THIS IS FETCH STORY LIST")
        println(this.adapter.snapshot().items)
    }

    private fun getToken(): String? {
        val sharedPreferences =
            getSharedPreferences(getString(R.string.my_preferences), Context.MODE_PRIVATE)
        return sharedPreferences.getString(getString(R.string.key_token), null)
    }
}

