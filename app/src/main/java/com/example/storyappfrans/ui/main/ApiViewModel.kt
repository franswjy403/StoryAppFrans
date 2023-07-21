package com.example.storyappfrans.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyappfrans.data.local.entity.Story
import com.example.storyappfrans.data.remote.response.BasicResponse
import com.example.storyappfrans.data.remote.response.LoginResponse
import com.example.storyappfrans.data.repository.ApiRepository
import com.example.storyappfrans.data.repository.Result
import java.io.File

class ApiViewModel(private val apiRepository: ApiRepository) : ViewModel() {

    fun getAllStories(
        authorization: String,
        location: Int?
    ): LiveData<PagingData<Story>> =
        apiRepository.getAllStories(authorization, location).cachedIn(viewModelScope)

    fun getAllStoriesList(
        authorization: String,
        page: Int?,
        size: Int?,
        location: Int?
    ): LiveData<Result<List<Story>>> =
        apiRepository.getAllStoriesList(authorization, page, size, location)

    fun getDetailStory(authorization: String, id: String): LiveData<Result<Story?>> =
        apiRepository.getDetailStory(authorization, id)

    fun addNewStory(
        authorization: String,
        description: String,
        photoFile: File
    ): LiveData<Result<BasicResponse>> =
        apiRepository.addNewStory(authorization, description, photoFile)

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> =
        apiRepository.login(email, password)

    fun register(name: String, email: String, password: String): LiveData<Result<BasicResponse>> =
        apiRepository.register(name, email, password)

    companion object {
        @Volatile
        private var instance: ApiViewModel? = null

        fun getInstance(apiRepository: ApiRepository): ApiViewModel =
            instance ?: synchronized(this) {
                instance ?: ApiViewModel(apiRepository).also { instance = it }
            }
    }
}
