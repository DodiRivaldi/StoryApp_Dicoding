package com.dodi.cerita.ui.activity.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.dodi.cerita.DummyData
import com.dodi.cerita.MainDispatcherRule
import com.dodi.cerita.abstraction.PagingDataTest
import com.dodi.cerita.data.CeritaRepository
import com.dodi.cerita.data.local.db.CeritaItem
import com.dodi.cerita.ui.adapter.CeritaAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var repository: CeritaRepository
    private lateinit var homeViewModel: HomeViewModel
    private val dummyData = DummyData.ceritaItemResponse()
    private val dummyToken = DummyData.dummyToken()

    @Before
    fun setup() {
        homeViewModel = HomeViewModel(repository)
    }

    @Test
    fun `when get Story Success`() = runTest {
        val data = PagingDataTest.snapshot(dummyData)
        val expectedResponse: Flow<PagingData<CeritaItem>> = flow {
            emit(data)
        }

        Mockito.`when`(repository.getStory(dummyToken)).thenReturn(
            expectedResponse
        )

        homeViewModel.getDataStory(dummyToken).observeForever {
            val asyncPaging = AsyncPagingDataDiffer(
                diffCallback = CeritaAdapter.CALLBACK,
                updateCallback = noopListUpdateCallback,
                mainDispatcher = mainDispatcherRule.testDispatcher,
                workerDispatcher = mainDispatcherRule.testDispatcher
            )
            CoroutineScope(Dispatchers.IO).launch {
                asyncPaging.submitData(it)
            }
            advanceUntilIdle()

            verify(repository).getStory(dummyToken)
            assertNotNull(asyncPaging.snapshot())
        }
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {

        }

        override fun onRemoved(position: Int, count: Int) {

        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {

        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {

        }

    }

    @Test
    fun `get Token success`() = runTest {
        val nameExpected = MutableLiveData<String>()
        nameExpected.value = dummyToken
        Mockito.`when`(repository.getToken()).thenReturn(nameExpected.asFlow())
        val nameActual = homeViewModel.getToken()
        verify(repository).getToken()
        assertNotNull(nameActual)
    }
}