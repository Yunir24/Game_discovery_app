package com.dauto.gamediscoveryapp.domain.usecase

import com.dauto.gamediscoveryapp.domain.GameRepository
import com.dauto.gamediscoveryapp.domain.entity.Game

class GetGameList (val gameRepository: GameRepository) {

    suspend fun getGameListByYear(): List<Game> {
        return gameRepository.getGameListByYear()
    }

    fun getGameListByGenres(): List<Game> {
        return gameRepository.getGameListByGenres()
    }

    fun getGameListByPlatforms(): List<Game> {
        return gameRepository.getGameListByPlatforms()
    }
}