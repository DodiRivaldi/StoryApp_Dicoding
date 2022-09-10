package com.dodi.cerita.abstraction.network

import com.dodi.cerita.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object{
        fun getApiConfig():ApiService{
            val interceptor = if (BuildConfig.DEBUG){
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            }else{
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .client(client).build()
            return retrofit.create(ApiService::class.java)
        }
    }
}