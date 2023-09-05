package com.dauto.gamediscoveryapp.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.dauto.gamediscoveryapp.data.local.AppDatabase
import com.dauto.gamediscoveryapp.data.network.ApiService
import com.dauto.gamediscoveryapp.data.network.paging.GameMediator
import com.dauto.gamediscoveryapp.data.network.paging.GamePagingSource
import com.dauto.gamediscoveryapp.domain.GameRepository
import com.dauto.gamediscoveryapp.domain.GameResult
import com.dauto.gamediscoveryapp.domain.entity.Game
import com.dauto.gamediscoveryapp.domain.entity.GameDetailInfo
import com.dauto.gamediscoveryapp.domain.entity.GameQuery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

const val NETWORK_PAGE_SIZE = 30

class GameRepositoryImpl @Inject constructor(
    private val mapper: GameMapper,
    private val appDataBase: AppDatabase,
    private val apiService: ApiService
) : GameRepository {
    companion object {
        const val TAG = "superTAG"
    }


    val pagingSourceFactory = {
        appDataBase.getPagingGameDao().getAllGameModel()
    }


    override suspend fun saveGameToLocal(game: Game) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteGameToLocal(id: Int) {
        TODO("Not yet implemented")
    }

    override fun getFavoriteGame(): Flow<Game> {
        TODO("Not yet implemented")
    }

    @OptIn(ExperimentalPagingApi::class)
   fun getGameListByYear(gameQuery: GameQuery): Flow<PagingData<Game>> {
        if (appDataBase == null) throw IllegalStateException("Database is not initialized")
        val mediator = GameMediator(gameQuery, apiService, appDataBase, mapper)
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = mediator
        ).flow.map { paging ->
            paging.map { dbModel ->
                mapper.gameDbModelToEntity(dbModel)
            }
        }
    }


    override fun getFavoriteGameList(): LiveData<GameResult<List<Game>>> = TODO()



    override fun getGameListPaging(gameQuery: GameQuery): Flow<PagingData<Game>> {
        val pageFactory = {
            GamePagingSource(service = apiService, gameQuery = gameQuery)
        }
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = true
            ), pagingSourceFactory = pageFactory
        ).flow.map { pagingData ->
            pagingData.map {
                mapper.gameDtoToEntity(it)
            }
        }
    }
//            .flowOn(Dispatchers.IO)}


    override suspend fun getGameInfo(gameId: Int): GameResult<GameDetailInfo> {
        return try {
            val gameInfo = apiService.getGameInfo(gameId = gameId)
            val series = apiService.getGameSeries(gameId = gameId)
            val screen = apiService.getGameScreenshots(gameId = gameId)
            if (gameInfo.isSuccessful && gameInfo.body() != null) {
                val gameBody = gameInfo.body() ?: throw NoSuchElementException()
                val first = mapper.gameDtoToEntity(gameBody)
                val second = screen.body()?.let {
                    mapper.listScreenDtoToEntity(it)
                } ?: emptyList()
                val third = series.body()?.let {
                    mapper.listGameDTOtoEntity(it.results)
                } ?: emptyList()

                GameResult.Success(
                    GameDetailInfo(
                        first, second, third
                    )
                )
            } else {
                GameResult.ApiError(gameInfo.message())
            }
        } catch (e: Exception) {
            GameResult.Exception(e.message ?: "Unknown error")
        }

    }


}


