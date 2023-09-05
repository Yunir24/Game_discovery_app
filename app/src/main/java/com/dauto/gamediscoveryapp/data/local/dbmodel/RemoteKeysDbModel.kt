package com.dauto.gamediscoveryapp.data.local.dbmodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeysDbModel(
    @PrimaryKey
    @ColumnInfo("repos_id")
    val reposId: Int,
    @ColumnInfo("prev_key")
    val prevKey: Int?,
    @ColumnInfo("next_key")
    val nextKey: Int?
)
