package com.dauto.gamediscoveryapp.data.local.dbmodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_item")
class GameDbModel(
    @PrimaryKey
    @ColumnInfo("game_id")
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
    val metacritic: Int,

    )