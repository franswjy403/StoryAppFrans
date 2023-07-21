package com.example.storyappfrans.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyappfrans.data.local.entity.Story
import com.example.storyappfrans.data.remote.StoryPagingSource
import com.example.storyappfrans.data.remote.response.BasicResponse
import com.example.storyappfrans.data.remote.response.GetAllStoriesResponse
import com.example.storyappfrans.data.remote.response.ListStoryItem
import com.example.storyappfrans.data.remote.response.LoginResponse
import com.example.storyappfrans.data.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ApiRepository(private val apiService: ApiService) {

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = apiService.login(email, password)
                emit(Result.Success(response))
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Unknown error occurred"
                emit(Result.Error(errorMessage))
            }
        }

    fun register(name: String, email: String, password: String): LiveData<Result<BasicResponse>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = apiService.register(name, email, password)
                emit(Result.Success(response))
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Unknown error occurred"
                emit(Result.Error(errorMessage))
            }
        }

    fun getAllStories(
        authorization: String, location: Int?
    ): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, authorization, location)
            }
        ).liveData
    }

    fun getAllStoriesList(
        authorization: String, page: Int?, size: Int?, location: Int?
    ): LiveData<Result<List<Story>>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response: GetAllStoriesResponse =
                apiService.getAllStories(authorization, page, size, location)
            if (response.error == false) {
                val listStoryItems: List<ListStoryItem?>? = response.listStory
                val storyList =
                    listStoryItems?.mapNotNull { it?.let { mapToStory(it) } } ?: emptyList()
                emit(Result.Success(storyList))
            } else {
                emit(Result.Error(response.message ?: ""))
            }
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Unknown error occurred"
            emit(Result.Error(errorMessage))
        }
    }

    fun getDetailStory(authorization: String, id: String): LiveData<Result<Story?>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = apiService.getDetailStory(authorization, id)
                if (response.error == false && response.story != null) {
                    val story = Story(
                        id = response.story.id ?: "",
                        userName = response.story.name ?: "",
                        photoUrl = response.story.photoUrl ?: "",
                        description = response.story.description ?: "",
                        lat = response.story.lat?.toDouble(),
                        lon = response.story.lon?.toDouble()
                    )
                    val mappedStory = mapToStory(story)
                    emit(Result.Success(mappedStory))
                } else {
                    emit(Result.Error(response.message ?: ""))
                }
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Unknown error occurred"
                emit(Result.Error(errorMessage))
            }
        }

    fun addNewStory(
        authorization: String,
        description: String,
        photoFile: File
    ): LiveData<Result<BasicResponse>> {
        return liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val descriptionRequestBody =
                    description.toRequestBody("text/plain".toMediaTypeOrNull())
                val photoRequestBody = photoFile.asRequestBody("image/*".toMediaTypeOrNull())
                val photoPart =
                    MultipartBody.Part.createFormData("photo", photoFile.name, photoRequestBody)

                val response =
                    apiService.addNewStory(authorization, descriptionRequestBody, photoPart)
                if (response.error == false) {
                    emit(Result.Success(response))
                } else {
                    emit(Result.Error(response.message ?: ""))
                }
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Unknown error occurred"
                emit(Result.Error(errorMessage))
            }
        }
    }

    private fun mapToStory(listStoryItem: ListStoryItem): Story {
        return with(listStoryItem) {
            Story(
                id = id.orEmpty(),
                userName = name.orEmpty(),
                photoUrl = photoUrl.orEmpty(),
                description = description.orEmpty(),
                lat = lat?.toDouble(),
                lon = lon?.toDouble()
            )
        }
    }

    private fun mapToStory(story: Story): Story {
        return story
    }

    companion object {
        @Volatile
        private var instance: ApiRepository? = null

        fun getInstance(apiService: ApiService): ApiRepository =
            instance ?: synchronized(this) {
                instance ?: ApiRepository(apiService).also { instance = it }
            }
    }
}
