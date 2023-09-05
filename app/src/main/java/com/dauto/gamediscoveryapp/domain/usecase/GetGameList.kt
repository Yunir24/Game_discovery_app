package com.dauto.gamediscoveryapp.domain.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.dauto.gamediscoveryapp.domain.GameRepository
import com.dauto.gamediscoveryapp.domain.GameResult
import com.dauto.gamediscoveryapp.domain.entity.Game
import com.dauto.gamediscoveryapp.domain.entity.GameDetailInfo
import com.dauto.gamediscoveryapp.domain.entity.GameQuery
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGameList @Inject constructor(
    private val gameRepository: GameRepository) {


    fun getFavoriteGameList(): LiveData<List<GameDetailInfo>> {
        return gameRepository.getFavoriteGameList()
    }

    fun getGameListByPaging(gameQuery: GameQuery): Flow<PagingData<Game>> {
        return gameRepository.getGameListPaging(gameQuery)
    }
}