package com.example.mymvvpapp.data.network

import com.example.mymvvpapp.data.model.Post
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
    @GET("/posts")
    suspend fun getAllPosts(): Response<List<Post>>

}