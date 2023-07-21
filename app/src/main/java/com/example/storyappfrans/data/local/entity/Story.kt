package com.example.storyappfrans.data.local.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(
    val id: String,
    val userName: String,
    val photoUrl: String,
    val description: String,
    val lat: Double?,
    val lon: Double?
) : Parcelable