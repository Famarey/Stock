package com.example.stock.ui.stocklist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stock.data.model.StockQuote

@Composable
fun StockListScreen(viewModel: StockViewModel, onSelect: (String) -> Unit) {
    Scaffold(
        topBar = {
            Box(modifier = Modifier.padding(16.dp)) {
                Text(text = "自选监控", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (viewModel.errorMessage != null) {
                Text(text = viewModel.errorMessage!!, modifier = Modifier.align(Alignment.Center), color = Color.Red)
            } else if (!viewModel.hasFavorites) {
                Text(text = "当前无自选", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(viewModel.stocks) { stock ->
                        StockListItem(stock, onSelect)
                    }
                }
            }
        }
    }
}

@Composable
fun StockListItem(stock: StockQuote,onSelect: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onSelect(stock.symbol) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = stock.symbol, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                Text(text = stock.companyName, fontSize = 12.sp, color = Color.Gray, maxLines = 1)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(text = "$${String.format("%.2f", stock.price)}", fontWeight = FontWeight.Bold)
                val color = if (stock.changePercent >= 0) Color(0xFF00C853) else Color.Red
                Text(
                    text = "${if (stock.changePercent >= 0) "+" else ""}${String.format("%.2f", stock.changePercent)}%",
                    color = color,
                    fontSize = 14.sp
                )
            }
        }
    }
}