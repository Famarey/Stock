package com.example.stock.data.model

import com.google.gson.annotations.SerializedName

data class HistoricalPrice(
    val symbol: String,
    val date: String,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Long,
    val change: Double,
    @SerializedName("changePercent")
    val changePercent: Double,
    val vwap: Double
)
