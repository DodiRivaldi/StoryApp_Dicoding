package com.dodi.cerita.ui.fragment.signin

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import com.dodi.cerita.DummyData
import com.dodi.cerita.MainDispatcherRule
import com.dodi.cerita.abstraction.Constant
import com.dodi.cerita.data.CeritaRepository
import com.dodi.cerita.data.remote.response.LoginResponse
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
class SignInViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var repository: CeritaRepository
    private lateinit var signInViewModel: SignInViewModel
    private val dummyData = DummyData.loginResponse()
    private val dummyEmail = "mnbv@mail.com"
    private val dummyPassword = "123456"

    @Before
    fun setup() {
        signInViewModel = SignInViewModel(repository)
    }

    @Test
    fun `when Sign Success`() = runTest {
        val expextedResponse: Flow<Result<LoginResponse>> = flow {
            emit(Result.success(dummyData))
        }
        `when`(repository.signIn(dummyEmail, dummyPassword)).thenReturn(
            expextedResponse
        )

        signInViewModel.signIn(dummyEmail, dummyPassword).asLiveData().observeForever {
            verify(repository).signIn(dummyEmail,dummyPassword)
            assertNotNull(dummyData.message)
        }

    }

    @Test
    fun `when Sign failed`() = runTest {
        val expectedResponse: Flow<Result<LoginResponse>> = flow {
            emit(Result.failure(Throwable("SampleError")))
        }

        `when`(repository.signIn(dummyEmail, dummyPassword)).thenReturn(
            expectedResponse
        )

        signInViewModel.signIn(dummyEmail, dummyPassword).asLiveData().observeForever {
            verify(repository).signIn(dummyEmail,dummyPassword)
            assertNull(it.getOrNull())
        }
    }


}