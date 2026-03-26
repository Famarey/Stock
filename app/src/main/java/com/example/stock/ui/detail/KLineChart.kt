package com.example.stock.ui.detail

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stock.data.model.HistoricalPrice

private val CANDLE_WIDTH = 15.dp
private val CANDLE_GAP = 5.dp
private val PRICE_LABEL_WIDTH = 50.dp

@Composable
fun KLineChart(historicalPrices: List<HistoricalPrice>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "日K线",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        if (historicalPrices.isEmpty()) {
            Text(
                text = "暂无数据",
                color = Color.Gray,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            val prices = historicalPrices.takeLast(60)
            val minPrice = prices.minOf { it.low }
            val maxPrice = prices.maxOf { it.high }
            val scrollState = rememberScrollState()
            
            LaunchedEffect(Unit) {
                scrollState.scrollTo(scrollState.maxValue)
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                PriceLabels(
                    minPrice = minPrice,
                    maxPrice = maxPrice
                )
                
                Column(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .horizontalScroll(scrollState)
                    ) {
                        Canvas(
                            modifier = Modifier
                                .width((CANDLE_WIDTH + CANDLE_GAP) * prices.size)
                                .height(200.dp)
                        ) {
                            drawKLines(
                                historicalPrices = prices,
                                minPrice = minPrice,
                                maxPrice = maxPrice
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(scrollState, enabled = false)
                    ) {
                        Box(
                            modifier = Modifier
                                .width((CANDLE_WIDTH + CANDLE_GAP) * prices.size)
                                .height(24.dp)
                        ) {
                            SimpleDateLabels(prices = prices)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PriceLabels(
    minPrice: Double,
    maxPrice: Double
) {
    Box(
        modifier = Modifier
            .width(PRICE_LABEL_WIDTH)
            .height(200.dp)
    ) {
        val priceRange = maxPrice - minPrice
        val step = priceRange / 4
        
        repeat(5) { index ->
            val price = maxPrice - step * index
            val yOffset = (index * 50).dp
            
            Text(
                text = String.format("%.2f", price),
                fontSize = 10.sp,
                color = Color.Gray,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 4.dp)
                    .offset(y = yOffset)
            )
        }
    }
}

@Composable
private fun SimpleDateLabels(prices: List<HistoricalPrice>) {
    val step = if (prices.size > 20) 5 else 3
    
    prices.forEachIndexed { index, price ->
        if (index % step == 0 || index == prices.size - 1) {
            val date = price.date.substring(5)
            val startPadding = (index * 20).dp
            
            Text(
                text = date,
                fontSize = 10.sp,
                color = Color.Gray,
                modifier = Modifier.padding(start = startPadding, top = 4.dp)
            )
        }
    }
}

private fun DrawScope.drawKLines(
    historicalPrices: List<HistoricalPrice>,
    minPrice: Double,
    maxPrice: Double
) {
    if (historicalPrices.isEmpty()) return
    
    val priceRange = maxPrice - minPrice
    
    if (priceRange <= 0) return
    
    val canvasHeight = size.height
    val candleWidth = CANDLE_WIDTH.toPx()
    val gap = CANDLE_GAP.toPx()
    
    historicalPrices.forEachIndexed { index, price ->
        val x = index * (candleWidth + gap)
        val centerX = x + candleWidth / 2
        
        val highY = canvasHeight - ((price.high - minPrice) / priceRange * canvasHeight).toFloat()
        val lowY = canvasHeight - ((price.low - minPrice) / priceRange * canvasHeight).toFloat()
        val openY = canvasHeight - ((price.open - minPrice) / priceRange * canvasHeight).toFloat()
        val closeY = canvasHeight - ((price.close - minPrice) / priceRange * canvasHeight).toFloat()
        
        val isUp = price.close >= price.open
        val color = if (isUp) Color(0xFF00C853) else Color.Red
        
        drawLine(
            color = color,
            start = Offset(centerX, highY),
            end = Offset(centerX, lowY),
            strokeWidth = 1f
        )
        
        val topY = minOf(openY, closeY)
        val bottomY = maxOf(openY, closeY)
        drawRect(
            color = color,
            topLeft = Offset(x, topY),
            size = androidx.compose.ui.geometry.Size(candleWidth, (bottomY - topY).toFloat())
        )
    }
}
