package com.example.stock.data.model

import com.google.gson.annotations.SerializedName

/**
 * 股票详情模型
 */
data class StockQuote(
    val symbol: String,      // 股票代码 (如: AAPL)
    @SerializedName("name")
    val companyName: String="N/A", // 公司名称
    val price: Double,       // 当前价格
    @SerializedName("changePercentage")
    val changePercent: Double, // 涨跌幅
    @SerializedName("change")
    val change: Double,       // 涨跌额
)