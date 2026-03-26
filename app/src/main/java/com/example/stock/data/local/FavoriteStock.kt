package com.example.stock.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_stocks")
data class FavoriteStock(
    @PrimaryKey val symbol: String // 股票代码作为主键
)