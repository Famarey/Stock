package com.example.stock.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {
    @Query("SELECT * FROM favorite_stocks")
    fun getAllFavorites(): Flow<List<FavoriteStock>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(stock: FavoriteStock)

    @Delete
    suspend fun delete(stock: FavoriteStock)

    @Query("SELECT EXISTS(SELECT * FROM favorite_stocks WHERE symbol = :symbol)")
    suspend fun isFavorite(symbol: String): Boolean
}