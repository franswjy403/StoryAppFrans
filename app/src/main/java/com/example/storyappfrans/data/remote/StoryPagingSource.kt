package com.example.storyappfrans.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyappfrans.data.local.entity.Story
import com.example.storyappfrans.data.remote.response.ListStoryItem
import com.example.storyappfrans.data.remote.retrofit.ApiService
import com.example.storyappfrans.data.repository.Result

class StoryPagingSource(
    private val apiService: ApiService,
    private val authorization: String,
    private val location: Int?
) : PagingSource<Int, Story>() {
    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData =
                apiService.getAllStories(authorization, position, params.loadSize, location)

            val listStoryItems: List<ListStoryItem?>? = responseData.listStory
            val storyList =
                listStoryItems?.mapNotNull { it?.let { mapToStory(it) } } ?: emptyList()

            println("STORY PAGING SOURCE STORY LIST")
            println(storyList)
            println(storyList.size)
            LoadResult.Page(
                data = storyList,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.listStory.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
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

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}