package com.dauto.gamediscoveryapp.domain

import androidx.paging.PagingData
import com.dauto.gamediscoveryapp.domain.entity.Game
import com.dauto.gamediscoveryapp.domain.entity.GameDetailInfo
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    suspend fun saveGameToLocal(game: Game)
    suspend fun deleteGameToLocal(id: Int)
    fun getListOfGameToLocal(): Flow<Game>

    fun getGameListByYear(): Flow<GameResult<Game>>

    fun getGameListByGenres(): Flow<GameResult<Game>>

    fun getGameListPaging(): Flow<PagingData<Game>>

    suspend fun getGameInfo(gameId: Int): GameDetailInfo

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