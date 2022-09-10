package com.dodi.cerita.ui.activity.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.dodi.cerita.DummyData
import com.dodi.cerita.MainDispatcherRule
import com.dodi.cerita.data.CeritaRepository
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
class MainViewModelTest{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var repository: CeritaRepository
    private lateinit var mainViewModel: MainViewModel

    private val dummyToken = DummyData.dummyToken()
    @Before
    fun setup(){
        mainViewModel = MainViewModel(repository)
    }

    @Test
    fun `get Token success`() = runTest {
        val nameExpected = MutableLiveData<String>()
        nameExpected.value = dummyToken
        `when`(repository.getToken()).thenReturn(nameExpected.asFlow())
        val nameActual = mainViewModel.getToken()
        verify(repository).getToken()
        assertNotNull(nameActual)
    }

}