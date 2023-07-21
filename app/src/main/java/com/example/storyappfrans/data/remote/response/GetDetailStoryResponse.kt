package com.example.storyappfrans.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class GetDetailStoryResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("story")
	val story: Story? = null
) : Parcelable

@Parcelize
data class Story(

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("lon")
	val lon: Float? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("lat")
	val lat: Float? = null
) : Parcelable
