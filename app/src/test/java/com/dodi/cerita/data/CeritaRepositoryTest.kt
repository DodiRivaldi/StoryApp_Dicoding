package com.dodi.cerita.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.dodi.cerita.DummyData
import com.dodi.cerita.FakeApiService
import com.dodi.cerita.MainDispatcherRule
import com.dodi.cerita.data.local.UserPref
import com.dodi.cerita.data.local.db.CeritaDb
import com.dodi.cerita.data.remote.response.CeritaResponse
import com.dodi.cerita.data.remote.response.DefaultResponse
import com.dodi.cerita.data.remote.response.LoginResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class CeritaRepositoryTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val apiService = FakeApiService()
    private val userPref = mock(UserPref::class.java)
    private val ceritaDb = mock(CeritaDb::class.java)
    private val repo = CeritaRepository(apiService,userPref,ceritaDb)

    private val dummySignIn = DummyData.loginResponse()
    private val dummyEmail = "mnbv@mail.com"
    private val dummyPassword = "123456"
    private val dummyName = "mnbv"
    private val dummyToken = DummyData.dummyToken()
    private val dummyDefaultResponse = DummyData.defaultResponse()
    private val dummyFile = DummyData.multipartFile()
    private val dummyDesc = DummyData.requestBody("Sample Desc")
    private val dummyLat = DummyData.requestBody("-8.2131231")
    private val dummyLon = DummyData.requestBody("-120.2309201")
    private val dummyDataCerita = DummyData.ceritaResponse()

    @Test
    fun `when Sign In Success`() = runTest {
        val item = MutableLiveData<LoginResponse>()
        item.value = dummySignIn

        val data = repo.signIn(dummyEmail,dummyPassword).asLiveData()
        Assert.assertNotNull(data)

    }

    @Test
    fun `get Token success`() = runTest {
        val nameExpected = MutableLiveData<String>()
        nameExpected.value = dummyToken
        `when`(repo.getToken()).thenReturn(nameExpected.asFlow())
        val nameActual = repo.getToken()
        Assert.assertNotNull(nameActual)
    }

    @Test
    fun `when Signup Success`() = runTest{
        val item = MutableLiveData<DefaultResponse>()
        item.value = dummyDefaultResponse

        val data = repo.signUp(dummyName,dummyEmail,dummyPassword).asLiveData()
        Assert.assertNotNull(data)
    }

    @Test
    fun `when Upload Success`() = runTest{
        val item = MutableLiveData<DefaultResponse>()
        item.value = dummyDefaultResponse

        val data = repo.uploadStory(dummyToken,dummyFile,dummyDesc,dummyLat,dummyLon).asLiveData()
        Assert.assertNotNull(data)
    }

    @Test
    fun `when Get Loc Story Success`() = runTest{
        val item = MutableLiveData<CeritaResponse>()
        item.value = dummyDataCerita

        val data = repo.getStoryLoc(dummyToken).asLiveData()
        Assert.assertNotNull(data)
    }

}