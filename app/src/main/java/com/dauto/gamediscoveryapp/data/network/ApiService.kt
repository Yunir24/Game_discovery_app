package com.dauto.gamediscoveryapp.data.network

import com.dauto.gamediscoveryapp.data.network.dto.ResultGameDto
import com.skydoves.sandwich.ApiResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
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
        @Query("key") key: String = KEY,
        @Query("page") page: Int
    ): ResultGameDto

    companion object{
        private const val GENRES =""
        private const val PLATFORMS =""
        private const val YEARS =""
        private const val KEY ="3219afc7b59647bf9ed8cba2971994d3"

    }
}