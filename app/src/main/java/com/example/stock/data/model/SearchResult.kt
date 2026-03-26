package com.example.stock.data.model

data class SearchResult(
    val symbol: String,
    val name: String,
    val currency: String,
    val stockExchange: String,
    val exchangeShortName: String
)