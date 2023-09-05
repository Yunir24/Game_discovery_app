package com.dauto.gamediscoveryapp.data.local.dbmodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "favorite_photo",
    indices = [Index("game_id")])
data class FavoriteImageDbModel
    (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "game_id")
    val gameId: Int,

    @ColumnInfo(name = "image_uri")
    val imageUri: String
)