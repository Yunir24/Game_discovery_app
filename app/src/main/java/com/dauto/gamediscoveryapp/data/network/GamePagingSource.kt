package com.dauto.gamediscoveryapp.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dauto.gamediscoveryapp.data.network.dto.GameDTO
import retrofit2.HttpException
import java.io.IOException

private const val START_PAGE = 1

class GamePagingSource(private val service: ApiService) : PagingSource<Int, GameDTO>() {


    override fun getRefreshKey(state: PagingState<Int, GameDTO>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GameDTO> {
        val pageIndex = params.key ?: START_PAGE
        return try{
            val result = service.getCallGameList(page = pageIndex)
            val games = result.results
            val nextKey = if (games.isEmpty()) {
                null
            } else {
                pageIndex + params.loadSize
            }
            LoadResult.Page(
                data = games,
                prevKey = if (pageIndex == START_PAGE) null else pageIndex,
                nextKey = nextKey
            )
        }catch (ex: IOException){
            return LoadResult.Error(ex)
        }catch (ex: HttpException){
            return LoadResult.Error(ex)
        }

    }
}