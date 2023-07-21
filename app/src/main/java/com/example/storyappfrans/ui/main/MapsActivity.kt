package com.example.storyappfrans.ui.main

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.paging.map
import com.example.storyappfrans.R
import com.example.storyappfrans.data.local.entity.Story
import com.example.storyappfrans.data.repository.Result
import com.example.storyappfrans.databinding.ActivityMapsBinding
import com.example.storyappfrans.utils.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val boundsBuilder = LatLngBounds.Builder()
    private lateinit var apiViewModel: ApiViewModel
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var errorTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val viewModelFactory = ViewModelFactory.getInstance()
        apiViewModel = ViewModelProvider(this, viewModelFactory)[ApiViewModel::class.java]

        loadingProgressBar = binding.progressBarLoading
        errorTextView = binding.textViewError
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        getMyLocation()
        setMapStyle()
        fetchStoryList()

        mMap.setOnMarkerClickListener { marker ->
            val story = marker.tag as? Story
            if (story != null) {
                val intent = Intent(this, StoryDetailActivity::class.java)
                intent.putExtra("story", story)
                startActivity(intent)
            }
            true
        }
    }


    private fun fetchStoryList() {
        val token = getToken()
        println(getString(R.string.bearer) + " " + (token ?: ""))
        apiViewModel.getAllStoriesList(
            getString(R.string.bearer) + " " + (token ?: ""),
            null,
            null,
            1
        ).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    loadingProgressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    loadingProgressBar.visibility = View.GONE

                    val storyList = result.data

                    if (storyList.isNotEmpty()) {
                        addManyMarker(storyList)
                    } else {
                    }
                }
                is Result.Error -> {
                    loadingProgressBar.visibility = View.GONE

                    errorTextView.text = result.error
                    errorTextView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun getToken(): String? {
        val sharedPreferences =
            getSharedPreferences(getString(R.string.my_preferences), Context.MODE_PRIVATE)
        return sharedPreferences.getString(getString(R.string.key_token), null)
    }

    private fun addManyMarker(storyList: List<Story>) {
        storyList.forEach { story ->
            val latLng = LatLng(story.lat ?: 0.0, story.lon ?: 0.0)
            val marker = mMap.addMarker(MarkerOptions().position(latLng).title(story.userName))
            marker?.tag = story
            boundsBuilder.include(latLng)
        }

        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    companion object {
        private const val TAG = "MapsActivity"
    }
}