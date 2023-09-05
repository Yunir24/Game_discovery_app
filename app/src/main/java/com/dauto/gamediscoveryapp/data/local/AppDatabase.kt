package com.dauto.gamediscoveryapp.data.local

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dauto.gamediscoveryapp.data.local.dbmodel.FavoriteGameDbModel
import com.dauto.gamediscoveryapp.data.local.dbmodel.FavoriteGenresDbModel
import com.dauto.gamediscoveryapp.data.local.dbmodel.FavoriteImageDbModel
import com.dauto.gamediscoveryapp.data.local.dbmodel.PlatformsDbModel


@Database(
    entities = [
        PlatformsDbModel::class,
        FavoriteGameDbModel::class,
        FavoriteImageDbModel::class,
        FavoriteGenresDbModel::class
    ], version = 2, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteGameDao(): FavoriteGameDao


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
                        .fallbackToDestructiveMigration()
                        .build()
                INSTANCE = db
                return db
            }
        }

    }
}