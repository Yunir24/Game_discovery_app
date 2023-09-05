package com.dauto.gamediscoveryapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dauto.gamediscoveryapp.data.local.dbmodel.RemoteKeysDbModel

@Dao
interface RemoteKeysDao {

    @Insert(entity = RemoteKeysDbModel::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeysDbModel>)

    @Query("SELECT * FROM remote_keys WHERE repos_id = :id")
    suspend fun remoteKeysGameId(id: Int): RemoteKeysDbModel?

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}