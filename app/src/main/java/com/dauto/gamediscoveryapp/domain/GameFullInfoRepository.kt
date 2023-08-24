package com.dauto.gamediscoveryapp.domain

import com.dauto.gamediscoveryapp.domain.entity.GameDetailInfo

interface GameFullInfoRepository {

    suspend fun getGameInfo(gameId: Int): GameDetailInfo

}