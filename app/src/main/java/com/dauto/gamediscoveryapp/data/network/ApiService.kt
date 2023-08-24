package com.dauto.gamediscoveryapp.data.network

import com.dauto.gamediscoveryapp.data.network.dto.GameDTO
import com.dauto.gamediscoveryapp.data.network.dto.ResultGameDto
import com.dauto.gamediscoveryapp.data.network.dto.ResultScreenshotsDto
import com.skydoves.sandwich.ApiResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {


    @GET("/api/games")
    suspend fun getGameList(
        @Query("key") key: String,
        @Query("page_size") pageSize: Int
    ): Response<ResultGameDto>

    @GET("/api/games")
    suspend fun getGameListByGenres(
        @Query("key") key: String,
        @Query("page_size") pageSize: Int,
        @Query("genres") genres: String
    ): ApiResponse<ResultGameDto>

    @GET("/api/games")
    suspend fun getGameListByPlatforms(
        @Query("key") key: String,
        @Query("page_size") pageSize: Int,
        @Query("platforms") platforms: String
    ): ApiResponse<ResultGameDto>


    @GET("/api/games")
    suspend fun getCallGameList(
        @Query("dates") yearsRange: String = YEARS,
        @Query("genres") genres: String = GENRES,
        @Query("platforms") platforms: String = PLATFORMS,
        @Query("key") key: String = KEY,
        @Query("page_size") pageSize: Int = DEFAULT_PAGE_SIZE,
        @Query("page") page: Int = DEFAULT_PAGE
    ): ResultGameDto

    @GET("/api/games/{id}")
    suspend fun getGameInfo(
        @Path("id")
        gameId: Int,
        @Query("key") key: String = KEY
    ): Response<GameDTO>

    @GET("/api/games/{id}/screenshots")
    suspend fun getGameScreenshots(
        @Path("id")
        gameId: Int,
        @Query("key") key: String = KEY
    ): Response<ResultScreenshotsDto>

    @GET("/api/games/{id}/game-series")
    suspend fun getGameSeries(
        @Path("id")
        gameId: Int,
        @Query("key") key: String = KEY
    ): Response<ResultGameDto>

    companion object {
        private const val GENRES = "racing,shooter,action,indie,adventure,rpg,strategy,casual,simulation,puzzle,arcade,platformer,massivelymultiplayer, sports, fighting, educational, card, family"
        private const val PLATFORMS = "1, 2, 3, 4, 8, 5, 6, 7, 11, 14"
        private const val YEARS = "1960,2020"
        private const val KEY = "3219afc7b59647bf9ed8cba2971994d3"
        private const val DEFAULT_PAGE = 1
        const val DEFAULT_PAGE_SIZE = 10

    }
}