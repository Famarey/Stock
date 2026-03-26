package com.example.stock.network

import com.example.stock.data.model.CompanyProfile
import com.example.stock.data.model.HistoricalPrice
import com.example.stock.data.model.SearchResult
import com.example.stock.data.model.StockQuote
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StockApiService {
    // 实时报价
    @GET("quote")
    suspend fun getStockQuotes(
        @Query("symbol") symbols: String,
        @Query("apikey") apiKey: String
    ): List<StockQuote>

    // 搜索公司名称
    @GET("search-name")
    suspend fun searchStock(
        @Query("query") query: String,
        @Query("apikey") apiKey: String
    ): List<SearchResult>

    // 获取详细信息
    @GET("profile")
    suspend fun getCompanyProfile(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String
    ): List<CompanyProfile>

    // 历史价格数据
    @GET("historical-price-eod/full")
    suspend fun getHistoricalPrice(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String
    ): List<HistoricalPrice>
}