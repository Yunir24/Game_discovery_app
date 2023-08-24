package com.dauto.gamediscoveryapp.domain.usecase

import com.dauto.gamediscoveryapp.domain.GameRepository
import com.dauto.gamediscoveryapp.domain.GameResult
import com.dauto.gamediscoveryapp.domain.entity.GameDetailInfo

class GetGameInfoUseCase(val gameRepository: GameRepository) {

    suspend fun getGameInfo(id: Int): GameDetailInfo{
        return gameRepository.getGameInfo(id)
    }
}