package com.example.storyappfrans.data.remote.retrofit

import com.example.storyappfrans.data.remote.response.LoginResponse
import com.example.storyappfrans.data.remote.response.BasicResponse
import com.example.storyappfrans.data.remote.response.GetAllStoriesResponse
import com.example.storyappfrans.data.remote.response.GetDetailStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): BasicResponse

    @Multipart
    @POST("stories")
    suspend fun addNewStory(
        @Header("Authorization") authorization: String,
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part
    ): BasicResponse

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("location") location: Int?
    ): GetAllStoriesResponse

    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): GetDetailStoryResponse

}