package com.dodi.cerita.ui.activity.upload

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.dodi.cerita.DummyData
import com.dodi.cerita.MainDispatcherRule
import com.dodi.cerita.data.CeritaRepository
import com.dodi.cerita.data.remote.response.DefaultResponse
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
class UploadViewModelTest{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var repository: CeritaRepository
    private lateinit var uploadViewModel: UploadViewModel
    private val dummyData = DummyData.defaultResponse()
    private val dummyFile = DummyData.multipartFile()
    private val dummyToken = "xas878djhsdbdbyads7bma3ui3jbdjudtad"
    private val dummyDesc = DummyData.requestBody("Sample Desc")
    private val dummyLat = DummyData.requestBody("-8.2131231")
    private val dummyLon = DummyData.requestBody("-120.2309201")

    @Before
    fun setup(){
        uploadViewModel = UploadViewModel(repository)
    }

    @Test
    fun `when Upload Success`() = runTest{
        val expextedResponse : Flow<Result<DefaultResponse>> = flow {
            emit(Result.success(dummyData))
        }
        `when`(repository.uploadStory(dummyToken,dummyFile,dummyDesc,dummyLat,dummyLon)).thenReturn(
            expextedResponse
        )

        uploadViewModel.upload(dummyToken,dummyDesc,dummyLat,dummyLon,dummyFile).asLiveData().observeForever {
            verify(repository).uploadStory(dummyToken,dummyFile,dummyDesc,dummyLat,dummyLon)
            assertNotNull(dummyData.message)
        }
    }

    @Test
    fun `when Upload failed`() = runTest {
        val expectedResponse : Flow<Result<DefaultResponse>> = flow {
            emit(Result.failure(Throwable("SampleError")))
        }

        `when`(repository.uploadStory(dummyToken,dummyFile,dummyDesc,dummyLat,dummyLon)).thenReturn(
            expectedResponse
        )

        uploadViewModel.upload(dummyToken,dummyDesc,dummyLat,dummyLon,dummyFile).asLiveData().observeForever {
            verify(repository).uploadStory(dummyToken,dummyFile,dummyDesc,dummyLat,dummyLon)
            assertNull(it.getOrNull())
        }
    }

    @Test
    fun `get Token success`() = runTest {
        val nameExpected = MutableLiveData<String>()
        nameExpected.value = dummyToken
        `when`(repository.getToken()).thenReturn(nameExpected.asFlow())
        val nameActual = uploadViewModel.getToken()
        verify(repository).getToken()
        assertNotNull(nameActual)
    }
}