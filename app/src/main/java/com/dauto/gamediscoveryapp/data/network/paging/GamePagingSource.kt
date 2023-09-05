package com.dauto.gamediscoveryapp.data.network.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dauto.gamediscoveryapp.data.network.ApiService
import com.dauto.gamediscoveryapp.data.network.dto.GameDTO
import com.dauto.gamediscoveryapp.domain.entity.GameQuery
import com.dauto.gamediscoveryapp.domain.entity.Genres
import com.dauto.gamediscoveryapp.domain.entity.ParentPlatforms
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException
import java.lang.Integer.max
import javax.inject.Inject

private const val START_PAGE = 1

class GamePagingSource (
    private val gameQuery: GameQuery,
    private val service: ApiService) :
    PagingSource<Int, GameDTO>() {


    override fun getRefreshKey(state: PagingState<Int, GameDTO>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        val x = anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
        return x
    }




    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GameDTO> {
        val pageIndex = params.key ?: START_PAGE
        val pageSize = max(params.loadSize, ApiService.DEFAULT_PAGE_SIZE)
        val searchQ = gameQuery.searchQuery ?: ""
        val yearQ = collectYearsListToString(gameQuery.date)
        val platQ = collectParentListToString(gameQuery.platforms)
        val genresQ = collectGenresListToString(gameQuery.genres)
        return try {
            val result = service.getCallGameList(
                page = pageIndex,
                pageSize = pageSize,
                search = searchQ,
                platforms = platQ,
                genres = genresQ,
                yearsRange = yearQ
            )
            val games = result.results

            val nextKey = if (games.isEmpty()) null else pageIndex + 1

            val prevKey = if (pageIndex == START_PAGE) null else pageIndex - 1
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


    private fun collectParentListToString(queryList: List<ParentPlatforms>): String{
        if (queryList.isEmpty()) return ApiService.PLATFORMS
        val stringCollector = StringBuilder()
        queryList.forEach {
            stringCollector.append("${it.id},")
        }
        stringCollector.deleteAt(stringCollector.length-1)
        return stringCollector.toString()
    }
    private fun collectGenresListToString(queryList: List<Genres>): String{
        if (queryList.isEmpty()) return ApiService.GENRES
        val stringCollector = StringBuilder()
        queryList.forEach {
            stringCollector.append("${it.id},")
        }
        stringCollector.deleteAt(stringCollector.length-1)
        return stringCollector.toString()
    }
    private fun collectYearsListToString(queryList: List<Float>): String{
        if (queryList.isEmpty()) return ApiService.YEARS
        val stringCollector = StringBuilder()
        queryList.forEach {
            stringCollector.append("${it.toInt()},")
        }
        stringCollector.deleteAt(stringCollector.length-1)
        return stringCollector.toString()
    }

}