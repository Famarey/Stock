package com.example.stock.ui.search

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stock.data.model.StockQuote
import com.example.stock.network.RetrofitClient
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val API_KEY = "jTnzHKufEGS1VXQG8H1BbCZBQ8KVQdnm" // 记得替换

    var searchQuery by mutableStateOf("")
    // 结果类型改为之前定义好的 StockQuote
    var searchResults by mutableStateOf<List<StockQuote>>(emptyList())
    var isSearching by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    private var searchJob: Job? = null

    fun onQueryChange(newQuery: String) {
        // 股票代码通常是大写，自动转换
        searchQuery = newQuery.uppercase()

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (searchQuery.isBlank()) {
                searchResults = emptyList()
                errorMessage = null
                return@launch
            }

            // 防抖：等待 800 毫秒
            delay(800)
            isSearching = true
            errorMessage = null

            viewModelScope.launch {
                isSearching = true
                errorMessage = null
                try {
                    val response = RetrofitClient.instance.getStockQuotes(searchQuery, API_KEY)
                    searchResults = response
                } catch (e: Exception) {
                    errorMessage = "加载失败: ${e.localizedMessage}"
                } finally {
                    isSearching = false
                }
            }
        }
    }
}