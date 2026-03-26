package com.example.stock.ui.detail

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stock.data.local.FavoriteStock
import com.example.stock.data.local.StockDao
import com.example.stock.data.model.CompanyProfile
import com.example.stock.data.model.HistoricalPrice
import com.example.stock.network.RetrofitClient
import kotlinx.coroutines.launch
import android.util.Log

class DetailViewModel(private val stockDao: StockDao) : ViewModel() {
    private val API_KEY = "jTnzHKufEGS1VXQG8H1BbCZBQ8KVQdnm"

    var profile by mutableStateOf<CompanyProfile?>(null)
    var historicalPrices by mutableStateOf<List<HistoricalPrice>>(emptyList())
    var isLoading by mutableStateOf(false)
    var isLoadingHistory by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isFavorite by mutableStateOf(false)

    fun fetchProfile(symbol: String) {
        viewModelScope.launch {
            isLoading = true
            Log.d("StockDetail", "开始请求: $symbol")
            try {
                val response = RetrofitClient.instance.getCompanyProfile(symbol, API_KEY)
                Log.d("StockDetail", "请求成功，返回数量: ${response.size}")
                if (response.isNotEmpty()) {
                    profile = response[0]
                    Log.d("StockDetail", "公司名称: ${profile?.companyName}")
                } else {
                    Log.e("StockDetail", "API 返回列表为空！")
                }
            } catch (e: Exception) {
                Log.e("StockDetail", "请求异常: ${e.message}")
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun fetchHistoricalPrices(symbol: String) {
        viewModelScope.launch {
            isLoadingHistory = true
            Log.d("StockDetail", "开始请求历史价格: $symbol")
            try {
                val response = RetrofitClient.instance.getHistoricalPrice(symbol, API_KEY)
                Log.d("StockDetail", "历史价格请求成功，返回数量: ${response.size}")
                historicalPrices = response.reversed()
            } catch (e: Exception) {
                Log.e("StockDetail", "历史价格请求异常: ${e.message}")
                e.printStackTrace()
            } finally {
                isLoadingHistory = false
            }
        }
    }

    fun checkFavorite(symbol: String) {
        viewModelScope.launch {
            isFavorite = stockDao.isFavorite(symbol)
        }
    }

    fun toggleFavorite(symbol: String) {
        viewModelScope.launch {
            if (isFavorite) {
                stockDao.delete(FavoriteStock(symbol))
                isFavorite = false
            } else {
                stockDao.insert(FavoriteStock(symbol))
                isFavorite = true
            }
        }
    }
}