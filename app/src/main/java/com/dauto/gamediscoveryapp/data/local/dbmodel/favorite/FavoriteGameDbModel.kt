package com.dauto.gamediscoveryapp.data.local.dbmodel.favorite

import androidx.room.*

@Entity(tableName = "favorite_game")
data class FavoriteGameDbModel(
    @PrimaryKey
    val id: Int,

    @ColumnInfo("name")
    val name: String,

    @ColumnInfo("description")
    val description: String,

    @ColumnInfo("released")
    val released: String,

    @ColumnInfo("background_image")
    val backgroundImage: String,

    @ColumnInfo("rating")
    val rating: Double,

    @ColumnInfo("rating_top")
    val ratingTop: Double,

    @ColumnInfo("metacritic")
    val metacritic: Int

)
