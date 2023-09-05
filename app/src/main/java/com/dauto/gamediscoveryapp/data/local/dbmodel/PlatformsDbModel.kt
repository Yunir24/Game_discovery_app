package com.dauto.gamediscoveryapp.data.local.dbmodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "platform_table",
    indices = [Index("game_id")]
)
data class PlatformsDbModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "platform_id")
    var id: Int = 0,

    @ColumnInfo(name = "game_id")
    val gameId: Int,

    @ColumnInfo(name = "platform_name")
    val platformName: String

)
