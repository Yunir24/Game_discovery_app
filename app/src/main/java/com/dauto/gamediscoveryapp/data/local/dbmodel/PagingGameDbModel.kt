package com.dauto.gamediscoveryapp.data.local.dbmodel

import androidx.room.Embedded
import androidx.room.Relation

data class PagingGameDbModel
    (
    @Embedded
    val gameDbModel: GameDbModel,

    @Relation(parentColumn = "game_id", entityColumn = "game_id")
    val genresList: List<GenresDbModel>,

    @Relation(parentColumn = "game_id", entityColumn = "game_id")
    val platformList: List<PlatformsDbModel>


            )