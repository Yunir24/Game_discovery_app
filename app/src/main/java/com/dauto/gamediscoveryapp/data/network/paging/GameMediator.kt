package com.dauto.gamediscoveryapp.data.network.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.dauto.gamediscoveryapp.data.GameMapper
import com.dauto.gamediscoveryapp.data.local.AppDatabase
import com.dauto.gamediscoveryapp.data.local.dbmodel.GameDbModel
import com.dauto.gamediscoveryapp.data.local.dbmodel.PagingGameDbModel
import com.dauto.gamediscoveryapp.data.local.dbmodel.RemoteKeysDbModel
import com.dauto.gamediscoveryapp.data.network.ApiService
import com.dauto.gamediscoveryapp.domain.entity.Game
import com.dauto.gamediscoveryapp.domain.entity.GameQuery
import com.dauto.gamediscoveryapp.domain.entity.Genres
import com.dauto.gamediscoveryapp.domain.entity.ParentPlatforms
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException


@ExperimentalPagingApi
class GameMediator(
    private val gameQuery: GameQuery,
    private val apiService: ApiService,
    private val appDatabase: AppDatabase,
    private val mapper: GameMapper
) : RemoteMediator<Int, PagingGameDbModel>() {

    private var currentIndex = 0

    override suspend fun initialize(): InitializeAction {
//        return InitializeAction.SKIP_INITIAL_REFRESH
        return super.initialize()
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, PagingGameDbModel>): MediatorResult {
        val loadTypesss = loadType
        val pageKeyData = getKeyPageData(loadType, state)
        val page = when (pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> pageKeyData as Int
        }
//        val page = pageKeyData ?: return MediatorResult.Success(endOfPaginationReached = true)
        val searchQ = gameQuery.searchQuery ?: ""
        val yearQ = collectYearsListToString(gameQuery.date )
        val platQ = collectParentListToString(gameQuery.platforms)
        val genresQ = collectGenresListToString(gameQuery.genres)
        try {
            val response = apiService.getCallGameList(
                yearsRange = yearQ,
                genres = genresQ,
                platforms = platQ,
                search = searchQ,
                page = page,
                pageSize = 20
            )
            val isEndOfList = response.results.isEmpty()
            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    appDatabase.getKeysDao().clearRemoteKeys()
                    appDatabase.getPagingGameDao().clearAllGameItem()
                }
                val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.results.map {
                    RemoteKeysDbModel(reposId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                appDatabase.getKeysDao().insertAll(keys)
                appDatabase.getPagingGameDao()
                    .insertAll(mapper.listGameDtoToDbModel(response.results))
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        }catch (exception: IOException){
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }

    }

    suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, PagingGameDbModel>): Any? {
        return when (loadType) {
            LoadType.REFRESH -> {
//                currentIndex=1
//                return 1
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
            }
            LoadType.APPEND -> {
//                return ++currentIndex
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
        }
    }


    private suspend fun getFirstRemoteKey(state: PagingState<Int, PagingGameDbModel>): RemoteKeysDbModel? {
        return state.pages
            .firstOrNull() {
                it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { game ->
                appDatabase.getKeysDao().remoteKeysGameId(game.gameDbModel.id)
            }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, PagingGameDbModel>): RemoteKeysDbModel? {
        return state.pages
            .lastOrNull() { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { game ->
                appDatabase.getKeysDao().remoteKeysGameId(game.gameDbModel.id)
            }
    }

    private suspend fun getClosestRemoteKey(state: PagingState<Int, PagingGameDbModel>): RemoteKeysDbModel? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.gameDbModel?.id?.let { reposId ->
                appDatabase.getKeysDao().remoteKeysGameId(reposId)
            }
        }
    }

    companion object {
        private const val DEFAULT_PAGE_INDEX = 1

        private fun collectParentListToString(queryList: List<ParentPlatforms>?): String{
            if (queryList == null || queryList.isEmpty()) return ApiService.PLATFORMS
            val stringCollector = StringBuilder()
            queryList.forEach {
                stringCollector.append(it.id)
                stringCollector.append(",")
            }
            stringCollector.deleteAt(stringCollector.length-1)
            return stringCollector.toString()
        }
        private fun collectGenresListToString(queryList: List<Genres>?): String{
            if (queryList == null || queryList.isEmpty()) return ApiService.GENRES
            val stringCollector = StringBuilder()
            queryList.forEach {
                stringCollector.append(it.id)
                stringCollector.append(",")
            }
            stringCollector.deleteAt(stringCollector.length-1)
            return stringCollector.toString()
        }
        private fun collectYearsListToString(queryList: List<Float>?): String{
            if (queryList == null || queryList.isEmpty()) return ApiService.YEARS
            val stringCollector = StringBuilder()
            queryList.forEach {
                stringCollector.append(it.toInt())
                stringCollector.append(",")
            }
            stringCollector.deleteAt(stringCollector.length-1)
            return stringCollector.toString()
        }


    }
}