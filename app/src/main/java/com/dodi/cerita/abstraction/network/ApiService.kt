package com.dodi.cerita.abstraction.network

import com.dodi.cerita.data.remote.response.CeritaResponse
import com.dodi.cerita.data.remote.response.DefaultResponse
import com.dodi.cerita.data.remote.response.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @GET("stories")
    suspend fun getStory(@Header("Authorization")token:String,
    @Query("page")page:Int?,
    @Query("size")size:Int?,
    @Query("location")location:Int?):CeritaResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(@Header("Authorization")token:String,
    @Part file :MultipartBody.Part,
    @Part("description")description:RequestBody,
    @Part("lat")lat:RequestBody?,
    @Part("lon")lon:RequestBody?):DefaultResponse


    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name")name:String,
        @Field("email")email :String,
        @Field("password")password:String
    ):DefaultResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email")email :String,
        @Field("password")password:String
    ):LoginResponse



}