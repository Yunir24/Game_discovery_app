package com.dauto.gamediscoveryapp.domain.usecase

import com.dauto.gamediscoveryapp.domain.GameRepository
import com.dauto.gamediscoveryapp.domain.GameResult
import com.dauto.gamediscoveryapp.domain.entity.GameDetailInfo
import javax.inject.Inject

class GetGameInfoUseCase @Inject constructor(
    private val gameRepository: GameRepository) {

    suspend fun getGameInfo(id: Int): GameResult<GameDetailInfo>{
        return gameRepository.getGameInfo(id)
    }
}