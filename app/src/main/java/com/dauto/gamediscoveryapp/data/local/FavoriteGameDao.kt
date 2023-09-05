package com.dauto.gamediscoveryapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dauto.gamediscoveryapp.data.local.dbmodel.*

@Dao
interface FavoriteGameDao {


    @Transaction
    suspend fun insertFavoriteGame(favoriteGame: FavoriteFullGameInfoDbModel) {
        setGameDb(favoriteGame.favoriteGameDbModel)
        setImageList(favoriteGame.listImageList)
        setGenresList(favoriteGame.listGenres)
        setPlatformList(favoriteGame.listPlatforms)
    }
    @Transaction
    suspend fun deleteFavorite(gameId: Int){
        deleteFavoriteGame(gameId)
        deleteFavoriteGenres(gameId)
        deleteFavoritePhoto(gameId)
        deleteFavoritePlatforms(gameId)
    }

    @Insert(entity = FavoriteGenresDbModel::class, OnConflictStrategy.REPLACE)
    suspend fun setGenresList(genres: List<FavoriteGenresDbModel>)

    @Insert(entity = PlatformsDbModel::class, OnConflictStrategy.REPLACE)
    suspend fun setPlatformList(genres: List<PlatformsDbModel>)

    @Insert(entity = FavoriteImageDbModel::class, OnConflictStrategy.REPLACE)
    suspend fun setImageList(platform: List<FavoriteImageDbModel>)

    @Insert(entity = FavoriteGameDbModel::class, OnConflictStrategy.REPLACE)
    suspend fun setGameDb(gameDb: FavoriteGameDbModel)

    @Query("DELETE FROM favorite_game WHERE id = :id")
    suspend fun deleteFavoriteGame(id: Int)

    @Query("DELETE FROM favorite_genres WHERE game_id = :id")
    suspend fun deleteFavoriteGenres(id: Int)

    @Query("DELETE FROM favorite_photo WHERE game_id = :id")
    suspend fun deleteFavoritePhoto(id: Int)

    @Query("DELETE FROM platform_table WHERE game_id = :id")
    suspend fun deleteFavoritePlatforms(id: Int)

    @Query("SELECT * FROM favorite_game WHERE id = :id")
    fun getExistFavoriteGame(id: Int): LiveData<FavoriteGameDbModel>



    @Transaction
    @Query("SELECT * FROM favorite_game WHERE id = :id")
    suspend fun getFavoriteGameItem(id: Int): FavoriteFullGameInfoDbModel?

    @Transaction
    @Query("SELECT * FROM favorite_game")
    fun getFavoriteGameList(): LiveData<List<FavoriteFullGameInfoDbModel>>

}