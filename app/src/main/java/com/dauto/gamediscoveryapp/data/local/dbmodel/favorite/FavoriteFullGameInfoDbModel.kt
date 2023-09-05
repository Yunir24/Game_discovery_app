package com.dauto.gamediscoveryapp.data.local.dbmodel.favorite

import androidx.room.Embedded
import androidx.room.Relation

data class FavoriteFullGameInfoDbModel (
    @Embedded
    val favoriteGameDbModel: FavoriteGameDbModel,

    @Relation(parentColumn = "id", entityColumn = "game_id")
    val listGenres: List<FavoriteGenresDbModel>,

    @Relation(parentColumn = "id", entityColumn = "game_id")
    val listImageList: List<FavoriteImageDbModel>
        )