package com.dauto.gamediscoveryapp.domain

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.dauto.gamediscoveryapp.domain.entity.Game
import com.dauto.gamediscoveryapp.domain.entity.GameDetailInfo
import com.dauto.gamediscoveryapp.domain.entity.GameQuery
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    suspend fun saveGameToLocal(gameDetail: GameDetailInfo)

    suspend fun deleteGameToLocal(id: Int)

    fun getExistFavoriteGame(id:Int): LiveData<Boolean>

    suspend fun getGameInfo(gameId: Int): GameResult<GameDetailInfo>

    fun getFavoriteGameList(): LiveData<List<GameDetailInfo>>

    fun getGameListPaging(gameQuery: GameQuery): Flow<PagingData<Game>>

}