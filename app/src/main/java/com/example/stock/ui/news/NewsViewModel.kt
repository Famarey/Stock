package com.example.stock.ui.news

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stock.data.model.NewsArticle
import com.example.stock.network.NewsRetrofitClient
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    private val FINNHUB_KEY = "d731jn9r01qn7f079sb0d731jn9r01qn7f079sbg"

    var newsList by mutableStateOf<List<NewsArticle>>(emptyList())
    var isLoading by mutableStateOf(false)
    var isRefreshing by mutableStateOf(false)

    init {
        fetchNews()
    }

    fun fetchNews(isPullToRefresh: Boolean = false) {
        viewModelScope.launch {
            if (isPullToRefresh) isRefreshing = true else isLoading = true
            try {
                newsList = NewsRetrofitClient.instance.getMarketNews(apiKey = FINNHUB_KEY)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
                isRefreshing = false
            }
        }
    }
}