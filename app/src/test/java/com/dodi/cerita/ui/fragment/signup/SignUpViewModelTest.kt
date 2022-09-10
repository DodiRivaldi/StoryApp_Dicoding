package com.dodi.cerita.ui.fragment.signup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import com.dodi.cerita.DummyData
import com.dodi.cerita.MainDispatcherRule
import com.dodi.cerita.data.CeritaRepository
import com.dodi.cerita.data.remote.response.DefaultResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SignUpViewModelTest{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var repository: CeritaRepository
    private lateinit var signUpViewModel: SignUpViewModel
    private val dummyEmail = "mnbv@mail.com"
    private val dummyPassword = "123456"
    private val dummyName = "mnbv"
    private val dummyData = DummyData.defaultResponse()

    @Before
    fun setup(){
        signUpViewModel = SignUpViewModel(repository)
    }

    @Test
    fun `when Signup Success`() = runTest{
        val expextedResponse : Flow<Result<DefaultResponse>> = flow {
            emit(Result.success(dummyData))
        }
        `when`(repository.signUp(dummyName,dummyEmail,dummyPassword)).thenReturn(
            expextedResponse
        )

        signUpViewModel.signUp(dummyName,dummyEmail,dummyPassword).asLiveData().observeForever {
            verify(repository).signUp(dummyName,dummyEmail,dummyPassword)
            assertNotNull(dummyData.message)
        }
    }

    @Test
    fun `when Signup failed`() = runTest {
        val expectedResponse : Flow<Result<DefaultResponse>> = flow {
            emit(Result.failure(Throwable("SampleError")))
        }

        `when`(repository.signUp(dummyName,dummyEmail,dummyPassword)).thenReturn(
            expectedResponse
        )

        signUpViewModel.signUp(dummyName,dummyEmail,dummyPassword).asLiveData().observeForever {
            verify(repository).signUp(dummyName,dummyEmail,dummyPassword)
            assertNull(it.getOrNull())
        }
    }
}
