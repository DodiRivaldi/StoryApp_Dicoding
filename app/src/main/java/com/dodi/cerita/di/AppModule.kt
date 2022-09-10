package com.dodi.cerita.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.dodi.cerita.abstraction.network.ApiConfig
import com.dodi.cerita.abstraction.network.ApiService
import com.dodi.cerita.data.local.UserPref
import com.dodi.cerita.data.local.db.CeritaDao
import com.dodi.cerita.data.local.db.CeritaDb
import com.dodi.cerita.data.local.db.KeyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("application")

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideApiService(): ApiService = ApiConfig.getApiConfig()

    @Provides
    fun provideDataSource(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore

    @Provides
    @Singleton
    fun provideUserPref(dataStore: DataStore<Preferences>): UserPref = UserPref(dataStore)

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): CeritaDb {
        return Room.databaseBuilder(context, CeritaDb::class.java, "cerita_db").build()
    }

    @Provides
    fun provideCeritaDao(ceritaDb: CeritaDb): CeritaDao = ceritaDb.ceritaDao()

    @Provides
    fun provideKeyDao(ceritaDb: CeritaDb): KeyDao = ceritaDb.keyDao()
}