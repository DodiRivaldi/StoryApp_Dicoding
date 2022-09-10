package com.dodi.cerita

import com.dodi.cerita.data.local.db.CeritaItem
import com.dodi.cerita.data.remote.response.CeritaResponse
import com.dodi.cerita.data.remote.response.DefaultResponse
import com.dodi.cerita.data.remote.response.ListStory
import com.dodi.cerita.data.remote.response.LoginResponse
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

object DummyData {
    fun defaultResponse() = DefaultResponse(
        false,
        "User created"
    )

    fun ceritaItemResponse(): List<CeritaItem> {
        val itemList = ArrayList<CeritaItem>()
        for (i in 0..10) {
            val item = CeritaItem(
                "story-FPkiCfHWfRpjwiY0",
                "Dicoding",
                "Jika sudah cinta programming, maka belajar ngoding pun terasa lebih mudah dan enjoy",
                "https://story-api.dicoding.dev/images/stories/photos-1648719567500_ssbAAkGs.png",
                "2022-03-31T09:39:27.502Z",
                -6.0019502,
                106.0662807,
            )
            itemList.add(item)
        }
        return itemList
    }


    fun loginResponse(): LoginResponse {
        val json = """
            {
              "error": false,
              "message": "success",
              "loginResult": {
                "userId": "user-Xitptirm6u88mRzy",
                "name": "mnbv",
                "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLVhpdHB0aXJtNnU4OG1SenkiLCJpYXQiOjE2NjI0NDMyMTJ9.0n0l384esUNi1fNsYQyERGAGaQiqspIytEXjmSmtnaI"
              }
            }
        """.trimIndent()
        return Gson().fromJson(json, LoginResponse::class.java)
    }

    fun multipartFile() = MultipartBody.Part.create("dummyFile".toRequestBody())

    fun requestBody(text: String) = text.toRequestBody()

    fun dummyToken(): String = "dummyToken"

    fun ceritaResponse(): CeritaResponse {
        val json = """
           {
    "error": false,
    "message": "Stories fetched successfully",
    "listStory": [
        {
            "id": "story-FvU4u0Vp2S3PMsFg",
            "name": "Dimas",
            "description": "Lorem Ipsum",
            "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
            "createdAt": "2022-01-08T06:34:18.598Z",
            "lat": -10.212,
            "lon": -16.002
        }
    ]
} 
        """.trimIndent()
        return Gson().fromJson(json, CeritaResponse::class.java)
    }
}