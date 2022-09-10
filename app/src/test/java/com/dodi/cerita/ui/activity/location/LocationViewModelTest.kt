package com.dodi.cerita.ui.activity.location

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.dodi.cerita.DummyData
import com.dodi.cerita.MainDispatcherRule
import com.dodi.cerita.data.CeritaRepository
import com.dodi.cerita.data.remote.response.CeritaResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LocationViewModelTest{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var repository: CeritaRepository
    private lateinit var locationViewModel: LocationViewModel
    private val dummyData = DummyData.ceritaResponse()
    private val dummyToken = DummyData.dummyToken()

    @Before
    fun setup(){
        locationViewModel = LocationViewModel(repository)
    }

    @Test
    fun `when Get Story Success`() = runTest{
        val expextedResponse : Flow<Result<CeritaResponse>> = flow {
            emit(Result.success(dummyData))
        }
        `when`(repository.getStoryLoc(dummyToken)).thenReturn(
            expextedResponse
        )

        locationViewModel.getDataStory(dummyToken).asLiveData().observeForever {
            verify(repository).getStoryLoc(dummyToken)
            assertNotNull(dummyData.message)
        }
    }

    @Test
    fun `when Get Story failed`() = runTest {
        val expectedResponse : Flow<Result<CeritaResponse>> = flow {
            emit(Result.failure(Throwable("SampleError")))
        }

        `when`(repository.getStoryLoc(dummyToken)).thenReturn(
            expectedResponse
        )

        locationViewModel.getDataStory(dummyToken).asLiveData().observeForever {
            verify(repository).getStoryLoc(dummyToken)
            assertNull(it.getOrNull())
        }
    }

    @Test
    fun `get Token success`() = runTest {
        val nameExpected = MutableLiveData<String>()
        nameExpected.value = dummyToken
        `when`(repository.getToken()).thenReturn(nameExpected.asFlow())
        val nameActual = locationViewModel.getToken()
        verify(repository).getToken()
        assertNotNull(nameActual)
    }

}
