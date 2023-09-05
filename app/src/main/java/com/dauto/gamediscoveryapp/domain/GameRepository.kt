package com.dauto.gamediscoveryapp.domain

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.dauto.gamediscoveryapp.domain.entity.Game
import com.dauto.gamediscoveryapp.domain.entity.GameDetailInfo
import com.dauto.gamediscoveryapp.domain.entity.GameQuery
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    suspend fun saveGameToLocal(game: Game)
    suspend fun deleteGameToLocal(id: Int)
    fun getFavoriteGame(): Flow<Game>

    suspend fun getGameInfo(gameId: Int): GameResult<GameDetailInfo>

    fun getFavoriteGameList(): LiveData<GameResult<List<Game>>>

    fun getGameListPaging(gameQuery: GameQuery): Flow<PagingData<Game>>


/*
* save delete game
*
* load gamelist
*
* load gameifo
* load gamePhoto
* load gameSeries
* */
}