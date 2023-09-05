package com.dauto.gamediscoveryapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.dauto.gamediscoveryapp.data.local.AppDatabase
import com.dauto.gamediscoveryapp.data.network.ApiService
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

    private val dao = appDataBase.favoriteGameDao()


    override suspend fun saveGameToLocal(gameDetail: GameDetailInfo) {
        val gameDb = mapper.gameEntityToDbModel(gameDetail)
        dao.insertFavoriteGame(gameDb)
    }

    override fun getExistFavoriteGame(id: Int): LiveData<Boolean> =
        MediatorLiveData<Boolean>().apply {
            addSource(dao.getExistFavoriteGame(id)){
                value = it != null
            }
        }

    override suspend fun deleteGameToLocal(id: Int) {
        dao.deleteFavorite(id)
    }


    override fun getFavoriteGameList(): LiveData<List<GameDetailInfo>> =
        MediatorLiveData<List<GameDetailInfo>>().apply {
            addSource(dao.getFavoriteGameList()) {
                value = mapper.listGameDbModelToEntity(it)
            }
        }


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


    override suspend fun getGameInfo(gameId: Int): GameResult<GameDetailInfo> {
        return try {
            val gameDb = dao.getFavoriteGameItem(gameId)
            if (gameDb != null) {
                GameResult.Success(
                    mapper.gameDbModelToEntity(gameDb)
                )
            } else {
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
            }
        } catch (e: Exception) {
            GameResult.Exception(e.message ?: "Unknown error")
        }

    }


}


