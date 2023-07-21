package com.example.storyappfrans.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyappfrans.data.local.entity.Story
import com.example.storyappfrans.data.repository.ApiRepository
import com.example.storyappfrans.utils.DataDummy
import com.example.storyappfrans.utils.MainDispatcherRule
import com.example.storyappfrans.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ApiViewModelTest {
    private val authToken =
        "Bearer " + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTBkQXprY0VmbVh5REZjVVciLCJpYXQiOjE2ODU5MzcxNTZ9.mX3SCzSJSLeNof7HrlZx3L114RnAYack6OZ4iRa4E3k"

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var apiRepository: ApiRepository

    private val dummyStories = DataDummy.GenerateDummyStoriesEntity()

    @Test
    fun `when getAllStories should not null and return success`() = runTest {
        val data: PagingData<Story> = PagingData.from(dummyStories)
        val expectedStories = MutableLiveData<PagingData<Story>>()
        expectedStories.value = data
        Mockito.`when`(apiRepository.getAllStories(authToken, 1)).thenReturn(expectedStories)

        val apiViewModel = ApiViewModel(apiRepository)
        val actualStories: PagingData<Story> =
            apiViewModel.getAllStories(authToken, 1).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = updateCallback,
            workerDispatcher = Dispatchers.Main
        )

        differ.submitData(actualStories)
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStories.size, differ.snapshot().size)
        Assert.assertEquals(dummyStories[0], differ.snapshot()[0])
    }

    @Test
    fun `when getAllStories null and return success`() = runTest {
        val expectedStories = MutableLiveData<PagingData<Story>>()
        expectedStories.value = PagingData.empty()
        Mockito.`when`(apiRepository.getAllStories("fake_token", 1)).thenReturn(expectedStories)

        val apiViewModel = ApiViewModel(apiRepository)
        val actualStories: PagingData<Story> =
            apiViewModel.getAllStories("fake_token", 1).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = updateCallback,
            workerDispatcher = Dispatchers.Main
        )

        differ.submitData(actualStories)
        println(differ.snapshot().size)
        Assert.assertEquals(0, differ.snapshot().size)
    }

    val updateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
        }

        override fun onRemoved(position: Int, count: Int) {
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
        }

    }
}
