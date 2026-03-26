package com.example.stock.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(symbol: String, viewModel: DetailViewModel, onBack: () -> Unit) {
    LaunchedEffect(symbol) {
        viewModel.fetchProfile(symbol)
        viewModel.checkFavorite(symbol)
        viewModel.fetchHistoricalPrices(symbol)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(symbol) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "返回")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleFavorite(symbol) }) {
                        Icon(
                            imageVector = if (viewModel.isFavorite) Icons.Default.Check else Icons.Default.Add,
                            contentDescription = if (viewModel.isFavorite) "取消自选" else "加自选"
                        )
                    }
                }
            )
        }
    ) { padding ->
        val data = viewModel.profile
        if (viewModel.isLoading || data == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // 头部信息
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = data.image,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(data.companyName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("${data.exchange} | ${data.industry}", color = Color.Gray)
                    }
                }

                Spacer(Modifier.height(24.dp))

                // 价格信息
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("$${data.price}", fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "${if (data.changes >= 0) "+" else ""}${data.changes}",
                        color = if (data.changes >= 0) Color(0xFF00C853) else Color.Red,
                        fontSize = 18.sp
                    )
                }

                Spacer(Modifier.height(24.dp))

                // K线图
                if (viewModel.isLoadingHistory) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    }
                } else {
                    KLineChart(viewModel.historicalPrices)
                }

                Divider(Modifier.padding(vertical = 16.dp))

                // 详细指标列表
                InfoRow("市值", "$${data.marketCap}")
                InfoRow("52周范围", data.range)
                InfoRow("CEO", data.ceo)
                InfoRow("员工人数", data.fullTimeEmployees)
                InfoRow("行业", data.sector)

                Spacer(Modifier.height(24.dp))

                Text("公司简介", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.height(8.dp))
                Text(data.description, lineHeight = 20.sp, color = Color.DarkGray)
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray)
        Text(value, fontWeight = FontWeight.Medium)
    }
}