package com.dauto.gamediscoveryapp.data.local

import androidx.paging.PagingSource
import androidx.room.*
import com.dauto.gamediscoveryapp.data.local.dbmodel.GameDbModel
import com.dauto.gamediscoveryapp.data.local.dbmodel.GenresDbModel
import com.dauto.gamediscoveryapp.data.local.dbmodel.PagingGameDbModel
import com.dauto.gamediscoveryapp.data.local.dbmodel.PlatformsDbModel

// посмотреть каскадное удаление и вставку

@Dao
interface GamePagingDao {

    @Transaction
    suspend fun insertAll(gameModel: List<PagingGameDbModel>)
    {
        gameModel.forEach { addPagingGameDbModel(it) }
    }

    @Transaction
    @Query("SELECT * FROM game_item ORDER BY game_id ASC")
    fun getAllGameModel(): PagingSource<Int, PagingGameDbModel>

    @Transaction
    suspend fun clearAllGameItem(){
        deleteGameTable()
        deleteGenresTable()
        deletePlatformTable()
    }

    @Insert(entity = GenresDbModel::class, OnConflictStrategy.REPLACE)
    suspend fun setGenresList(genres: List<GenresDbModel>)

    @Insert(entity = PlatformsDbModel::class, OnConflictStrategy.REPLACE)
    suspend fun setPlatformList(platform: List<PlatformsDbModel>)

    @Insert(entity = GameDbModel::class, OnConflictStrategy.REPLACE)
    suspend fun setGameDb(gameDb: GameDbModel)

    @Transaction
    suspend fun addPagingGameDbModel(gamePagingDb: PagingGameDbModel){
        setGameDb(gamePagingDb.gameDbModel)
        setGenresList(gamePagingDb.genresList)
        setPlatformList(gamePagingDb.platformList)
    }

    @Query("DELETE FROM game_item")
    suspend fun deleteGameTable()

    @Query("DELETE FROM genres_table")
    suspend fun deleteGenresTable()

    @Query("DELETE FROM platform_table")
    suspend fun deletePlatformTable()

}