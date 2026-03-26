package com.example.stock.ui.stocklist

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stock.data.local.StockDao
import com.example.stock.data.model.StockQuote
import com.example.stock.network.RetrofitClient
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StockViewModel(private val stockDao: StockDao) : ViewModel() {
    private val API_KEY = "jTnzHKufEGS1VXQG8H1BbCZBQ8KVQdnm"

    // UI 状态
    var stocks by mutableStateOf<List<StockQuote>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var hasFavorites by mutableStateOf(true)

    init {
        viewModelScope.launch {
            stockDao.getAllFavorites().collectLatest { favorites ->
                hasFavorites = favorites.isNotEmpty()
                if (hasFavorites) {
                    fetchStockData(favorites.map { it.symbol })
                } else {
                    stocks = emptyList()
                }
            }
        }
    }

    fun fetchStockData(symbols: List<String>) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val result = coroutineScope {
                    val deferredList = symbols.map { symbol ->
                        async {
                            try {
                                val response = RetrofitClient.instance.getStockQuotes(symbol, API_KEY)
                                if (response.isNotEmpty()) {
                                    val stock = response.first()
                                    Log.d("StockDebug", "股票 $symbol - changePercent: ${stock.changePercent}")
                                }
                                response.firstOrNull()
                            } catch (e: Exception) {
                                Log.e("StockDebug", "获取 $symbol 失败: ${e.message}")
                                null
                            }
                        }
                    }
                    deferredList.awaitAll().filterNotNull()
                }

                stocks = result
            } catch (e: Exception) {
                errorMessage = "加载失败: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }
}