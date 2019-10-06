package com.example.tp2et3.network

import com.example.tp2et3.Article
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleService {

    @GET("everything")
    fun bitcoins(@Query("q") query: String,
                      @Query("from") date: String,
                      @Query("sortBy") filter: String,
                      @Query("apiKey") apiKey: String)
            : Call<List<Article>>

}
