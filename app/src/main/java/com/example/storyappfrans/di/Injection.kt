package com.example.storyappfrans.di

import com.example.storyappfrans.data.remote.retrofit.ApiConfig
import com.example.storyappfrans.data.repository.ApiRepository

object Injection {
    fun provideApiRepository(): ApiRepository {
        val apiService = ApiConfig.getApiService()
        return ApiRepository.getInstance(apiService)
    }
}