package com.dauto.gamediscoveryapp.data.local.dbmodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "genres_table",
    indices = [Index("game_id")]
)
data class GenresDbModel (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("genres_id")
    var id: Int = 0,

    @ColumnInfo(name = "game_id")
    val gameId: Int,

    @ColumnInfo(name = "genres")
    val genres: String
        )
