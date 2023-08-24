package com.dauto.gamediscoveryapp.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.dauto.gamediscoveryapp.data.network.ApiFactory
import com.dauto.gamediscoveryapp.data.network.GamePagingSource
import com.dauto.gamediscoveryapp.domain.GameRepository
import com.dauto.gamediscoveryapp.domain.GameResult
import com.dauto.gamediscoveryapp.domain.entity.Game
import com.dauto.gamediscoveryapp.domain.entity.GameDetailInfo
import com.skydoves.sandwich.*
import com.skydoves.sandwich.retry.RetryPolicy
import com.skydoves.sandwich.retry.runAndRetry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

const val NETWORK_PAGE_SIZE = 15

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

    override fun getGameListByYear() = flow<GameResult<Game>> {
//        runAndRetry(retryPolicy) { attempt: Int, reason: String? ->
//            apiService.getCallGameList("3219afc7b59647bf9ed8cba2971994d3", pageSize = 5)
//                .suspendOnSuccess {
//                Log.e(TAG, reason.toString())
//                Log.e(TAG, data.toString())
//                Log.e(TAG, headers.toString())
//                emit(GameResult.Success(mapper.listGameDTOtoEntity(data.results)))
//            }.suspendOnError {
//                Log.e(TAG, statusCode.toString())
//                emit(GameResult.ApiError(statusCode.toString()))
//            }.suspendOnException {
//                message?.also {
//                    emit(GameResult.Exception(it))
//                }
//            }
//        }
    }.flowOn(Dispatchers.IO)

    //        val resp = apiService.getGameList("3219afc7b59647bf9ed8cba2971994d3", 5)
//        if (resp.isSuccessful) {
//            return mapper.listGameDTOtoEntity(resp.body()?.results!!)
//        } else {
//            Log.e(TAG, resp.message())
//        }
//        return emptyList()
//    }
    val retryPolicy = object : RetryPolicy {
        override fun shouldRetry(attempt: Int, message: String?): Boolean = attempt <= 3

        override fun retryTimeout(attempt: Int, message: String?): Int = 3000
    }

    override fun getGameListByGenres(): Flow<GameResult<Game>> = TODO()

//        flow<GameResult<Game>> {
//            runAndRetry(retryPolicy) { attempt: Int, reason: String? ->
//                apiService.getGameListByGenres(
//                    "3219afc7b59647bf9ed8cba2971994d3",
//                    pageSize = 5,
//                    genres = "action, indie"
//                ).suspendOnSuccess {
//                    Log.e(TAG, reason.toString())
//                    Log.e(TAG, data.toString())
//                    Log.e(TAG, headers.toString())
//                    emit(GameResult.Success(mapper.listGameDTOtoEntity(data.results)))
//                }.suspendOnError {
//                    Log.e(TAG, statusCode.toString())
//                    emit(GameResult.ApiError(statusCode.toString()))
//                }.suspendOnException {
//                    message?.also {
//                        emit(GameResult.Exception(it))
//                    }
//                }
//            }
//        }.flowOn(Dispatchers.IO)


    override fun getGameListPaging() =
        Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false
            ), pagingSourceFactory = {
                GamePagingSource(service = apiService)
            }
        ).flow.map { pagingData ->
            pagingData.map {
                mapper.gameDtoToEntity(it)
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun getGameInfo(gameId: Int): GameDetailInfo {

        val gameInfo = apiService.getGameInfo(gameId = gameId)
        val series = apiService.getGameSeries(gameId = gameId)
        val screen = apiService.getGameScreenshots(gameId = gameId)
        val first = mapper.gameDtoToEntity(gameInfo.body()!!)
        val second = screen.body()?.let {
            mapper.listScreenDtoToEntity(it)
        } ?: emptyList()
        val third = series.body()?.let {
            mapper.listGameDTOtoEntity(it.results)
        } ?: emptyList()

        return GameDetailInfo(
            first, second, third
        )


    }


}


