package com.example.stock.data.model

import com.google.gson.annotations.SerializedName

data class CompanyProfile(
    val symbol: String,
    val price: Double,
    val companyName: String,
    val currency: String,
    val exchange: String,
    val industry: String,
    val website: String,
    val description: String,
    val ceo: String,
    val sector: String,
    val image: String,       // 公司 Logo 链接
    val marketCap: Double,
    val range: String,       // 52周波动范围
    @SerializedName("change")
    val changes: Double,     // 对应 JSON 中的 change
    val fullTimeEmployees: String
)