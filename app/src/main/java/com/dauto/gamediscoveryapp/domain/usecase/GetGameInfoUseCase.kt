package com.dauto.gamediscoveryapp.domain.usecase

import androidx.lifecycle.LiveData
import com.dauto.gamediscoveryapp.domain.GameRepository
import com.dauto.gamediscoveryapp.domain.GameResult
import com.dauto.gamediscoveryapp.domain.entity.GameDetailInfo
import javax.inject.Inject

class GetGameInfoUseCase @Inject constructor(
    private val gameRepository: GameRepository) {

    suspend fun getGameInfo(id: Int): GameResult<GameDetailInfo>{
        return gameRepository.getGameInfo(id)
    }
    suspend fun saveGameToFavorite(gameDetailInfo: GameDetailInfo){
        gameRepository.saveGameToLocal(gameDetailInfo)
    }
    suspend fun deleteGameToFavorite(id: Int){
        return gameRepository.deleteGameToLocal(id)
    }
    fun getGameExist(id: Int): LiveData<Boolean>{
        return gameRepository.getExistFavoriteGame(id)
    }



}