package com.dauto.gamediscoveryapp.domain.usecase

import androidx.paging.PagingData
import com.dauto.gamediscoveryapp.domain.GameRepository
import com.dauto.gamediscoveryapp.domain.GameResult
import com.dauto.gamediscoveryapp.domain.entity.Game
import kotlinx.coroutines.flow.Flow

class GetGameList( val gameRepository: GameRepository) {

     fun getGameListByYear(): Flow<GameResult<Game>> {
        return gameRepository.getGameListByYear()
    }

    fun getGameListByGenres(): Flow<GameResult<Game>> {
        return gameRepository.getGameListByGenres()
    }

    fun getGameListByPlatforms(): Flow<PagingData<Game>> {
        return gameRepository.getGameListPaging()
    }
}