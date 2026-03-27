package com.example.stock.data.model

data class NewsArticle(
    val id: Long,
    val headline: String,    // 标题
    val summary: String,     // 摘要
    val url: String,         // 原文链接
    val source: String,      // 来源
    val datetime: Long,      // 时间戳
    val image: String        // 封面图
)