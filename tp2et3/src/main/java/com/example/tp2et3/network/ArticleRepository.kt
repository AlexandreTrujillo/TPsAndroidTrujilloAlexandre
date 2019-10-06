package com.example.tp2et3.network

import com.example.tp2et3.Article
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate


class ArticleRepository {

    private val cle = "4b4fa6b31241482c971ceb94852954f6"
    private val dateMoisDernier = LocalDate.now().minusMonths(1).toString()
    private var service:ArticleService

    init {
        val retrofit = Retrofit.Builder().apply {
            baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
        }.build()
        service = retrofit.create(ArticleService::class.java)
    }

    fun bitcoins(): List<Article> {
        val response = service.bitcoins("bitcoin",dateMoisDernier,"publishedAt",cle).execute()
        return (response.body() ?: emptyList())
    }
}