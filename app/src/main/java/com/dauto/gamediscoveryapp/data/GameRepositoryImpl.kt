package com.dauto.gamediscoveryapp.data

import android.util.Log
import com.dauto.gamediscoveryapp.data.network.ApiFactory
import com.dauto.gamediscoveryapp.domain.GameRepository
import com.dauto.gamediscoveryapp.domain.entity.Game
import com.skydoves.sandwich.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameRepositoryImpl(
    val scope: CoroutineScope
) : GameRepository {
    companion object {
        const val TAG = "superTAG"
    }

    private val apiService = ApiFactory.service
    private val mapper = GameMapper()

    override suspend fun saveGameToLocal(game: Game) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteGameToLocal(id: Int) {
        TODO("Not yet implemented")
    }

    override fun getListOfGameToLocal(): Flow<Game> {
        TODO("Not yet implemented")
    }

    override suspend fun getGameListByYear(): List<Game> {
        getGameListByGenres()
        val resp = apiService.getGameList("3219afc7b59647bf9ed8cba2971994d3", 5)
        if (resp.isSuccessful) {
            return mapper.listGameDTOtoEntity(resp.body()?.results!!)
        } else {
            Log.e(TAG, resp.message())
        }


        return emptyList()
    }

    override fun getGameListByGenres(): List<Game> {
        scope.launch{
            withContext(Dispatchers.IO) {
                apiService.getCallGameList("3219afc7b59647bf9ed8cba2971994d3", 5).onSuccess {


                            Log.e(TAG,statusCode.toString())
                            Log.e(TAG,data.toString())
                            Log.e(TAG,headers.toString())

            }.onError {  }.onException {  }
        }}
        return emptyList()
    }

    override fun getGameListByPlatforms(): List<Game> {
        TODO("Not yet implemented")
    }

    override fun getGameDetailInfo(): Game {
        TODO("Not yet implemented")
    }

}