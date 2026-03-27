package com.example.stock.network

import com.example.stock.data.model.NewsArticle
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    /**
     * 获取市场新闻
     * 完整路径示例：https://finnhub.io/api/v1/news?category=general&token=KEY
     */
    @GET("https://finnhub.io/api/v1/news")
    suspend fun getMarketNews(
        @Query("category") category: String = "general",
        @Query("token") apiKey: String
    ): List<NewsArticle>
}