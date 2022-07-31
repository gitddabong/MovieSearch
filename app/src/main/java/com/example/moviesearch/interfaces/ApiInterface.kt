package com.example.moviesearch.interfaces

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("search/{type}")
    fun getSearchResult(
        @Header("X-Naver-Client-Id") clientId: String,
        @Header("X-Naver-Client-Secret") clientSecret: String,
        @Path("type") type: String,
        @Query("query") query: String,
        @Query("display") display: Int? = null
    ): Call<String>
}