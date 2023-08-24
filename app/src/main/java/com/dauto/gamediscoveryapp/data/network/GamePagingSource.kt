package com.dauto.gamediscoveryapp.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dauto.gamediscoveryapp.data.network.dto.GameDTO
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

private const val START_PAGE = 1
private const val LOAD_DELAY_MILLIS = 3_000L
class GamePagingSource(private val service: ApiService) : PagingSource<Int, GameDTO>() {


    override fun getRefreshKey(state: PagingState<Int, GameDTO>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GameDTO> {
        val pageIndex = params.key ?: START_PAGE
        val pageSize = params.loadSize.coerceAtMost(ApiService.DEFAULT_PAGE_SIZE)
        if (pageIndex != START_PAGE) delay(LOAD_DELAY_MILLIS)
        return try {
            val result = service.getCallGameList(page = pageIndex, pageSize = pageSize)
            val games = result.results

            val nextKey = if (games.isEmpty()) null else pageIndex + 1

            val prevKey = if (pageIndex > START_PAGE - 1) pageIndex - 1 else null
            LoadResult.Page(
                data = games,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (ex: IOException) {
            return LoadResult.Error(ex)
        } catch (ex: HttpException) {
            return LoadResult.Error(ex)
        }

    }
}