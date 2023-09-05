package com.dauto.gamediscoveryapp.data.local

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dauto.gamediscoveryapp.data.local.dbmodel.GameDbModel
import com.dauto.gamediscoveryapp.data.local.dbmodel.GenresDbModel
import com.dauto.gamediscoveryapp.data.local.dbmodel.PlatformsDbModel
import com.dauto.gamediscoveryapp.data.local.dbmodel.RemoteKeysDbModel
import com.dauto.gamediscoveryapp.data.local.dbmodel.favorite.FavoriteGameDbModel
import com.dauto.gamediscoveryapp.data.local.dbmodel.favorite.FavoriteGenresDbModel
import com.dauto.gamediscoveryapp.data.local.dbmodel.favorite.FavoriteImageDbModel


@Database(
    entities = [
        GameDbModel::class,
        GenresDbModel::class,
        PlatformsDbModel::class,
        RemoteKeysDbModel::class,
        FavoriteGameDbModel::class,
        FavoriteImageDbModel::class,
        FavoriteGenresDbModel::class
    ], version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getPagingGameDao(): GamePagingDao
    abstract fun getKeysDao(): RemoteKeysDao


    companion object {

        private val lock = Any()
        private const val DB_NAME = "gamed.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(application: Application): AppDatabase {
            INSTANCE?.let {
                return it
            }
            synchronized(lock) {
                INSTANCE?.let {
                    return it
                }
                val db =
                    Room.databaseBuilder(application, AppDatabase::class.java, DB_NAME)
                        .build()
                INSTANCE = db
                return db
            }
        }

    }
}