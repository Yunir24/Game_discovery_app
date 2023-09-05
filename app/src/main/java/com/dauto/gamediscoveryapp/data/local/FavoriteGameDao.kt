package com.dauto.gamediscoveryapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dauto.gamediscoveryapp.data.local.dbmodel.GameDbModel
import com.dauto.gamediscoveryapp.data.local.dbmodel.GenresDbModel
import com.dauto.gamediscoveryapp.data.local.dbmodel.PlatformsDbModel
import com.dauto.gamediscoveryapp.data.local.dbmodel.favorite.FavoriteFullGameInfoDbModel
import com.dauto.gamediscoveryapp.data.local.dbmodel.favorite.FavoriteGameDbModel
import com.dauto.gamediscoveryapp.data.local.dbmodel.favorite.FavoriteGenresDbModel
import com.dauto.gamediscoveryapp.data.local.dbmodel.favorite.FavoriteImageDbModel

@Dao
interface FavoriteGameDao {


    @Transaction
    suspend fun insertFavoriteGame(favoriteGame: FavoriteFullGameInfoDbModel) {
        setGameDb(favoriteGame.favoriteGameDbModel)
        setImageList(favoriteGame.listImageList)
        setGenresList(favoriteGame.listGenres)
    }


    @Insert(entity = FavoriteGenresDbModel::class, OnConflictStrategy.REPLACE)
    suspend fun setGenresList(genres: List<FavoriteGenresDbModel>)

    @Insert(entity = FavoriteImageDbModel::class, OnConflictStrategy.REPLACE)
    suspend fun setImageList(platform: List<FavoriteImageDbModel>)

    @Insert(entity = FavoriteGameDbModel::class, OnConflictStrategy.REPLACE)
    suspend fun setGameDb(gameDb: FavoriteGameDbModel)

    @Query("DELETE FROM favorite_game WHERE id = :id")
    suspend fun deleteFavoriteGame(id: Int)

    @Query("SELECT * FROM favorite_game WHERE id = :id")
    suspend fun getFavoriteGameItem(id: Int): FavoriteFullGameInfoDbModel?

    @Query("SELECT * FROM favorite_game")
    fun getFavoriteGameList(): LiveData<List<FavoriteFullGameInfoDbModel>>

}