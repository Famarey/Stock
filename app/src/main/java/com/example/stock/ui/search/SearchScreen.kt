package com.example.stock.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.stock.ui.stocklist.StockListItem

@Composable
fun SearchScreen(viewModel: SearchViewModel,onSelect: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // 搜索输入框
        OutlinedTextField(
            value = viewModel.searchQuery,
            onValueChange = { viewModel.onQueryChange(it) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("输入精确的股票代码 (如 AAPL, TSLA)") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            // 可以在这里设置键盘类型，如果是纯字母代码可以优化键盘弹出
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 状态分发展示
        Box(modifier = Modifier.fillMaxSize()) {
            if (viewModel.isSearching) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.TopCenter))
            } else if (viewModel.errorMessage != null) {
                Text(
                    text = viewModel.errorMessage!!,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.TopCenter).padding(top = 16.dp)
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(viewModel.searchResults) { stock ->
                        // 直接调用现成的 UI 组件！
                        StockListItem(stock = stock,onSelect =onSelect)
                    }
                }
            }
        }
    }
}