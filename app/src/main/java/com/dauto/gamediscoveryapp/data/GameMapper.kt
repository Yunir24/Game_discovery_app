package com.dauto.gamediscoveryapp.data

import com.dauto.gamediscoveryapp.data.local.dbmodel.GameDbModel
import com.dauto.gamediscoveryapp.data.local.dbmodel.GenresDbModel
import com.dauto.gamediscoveryapp.data.local.dbmodel.PagingGameDbModel
import com.dauto.gamediscoveryapp.data.local.dbmodel.PlatformsDbModel
import com.dauto.gamediscoveryapp.data.network.dto.*
import com.dauto.gamediscoveryapp.domain.entity.Game
import com.dauto.gamediscoveryapp.domain.entity.ParentPlatforms
import javax.inject.Inject

class GameMapper @Inject constructor() {
    fun gameDtoToEntity(gameDTO: GameDTO) =
        Game(
            id = gameDTO.id,
            name = gameDTO.name,
            description = gameDTO.description,
            released = gameDTO.released,
            backgroundImage = gameDTO.backgroundImage,
            rating = gameDTO.rating,
            ratingTop = gameDTO.ratingTop,
            metacritic = gameDTO.metacritic.toString(),
            platforms = mapPlatformsList(gameDTO.platforms),
            genres = mapGenresList(gameDTO.genres)
        )


    fun gameDbModelToEntity(pagingGameDbModel: PagingGameDbModel): Game {
        val gameDb = pagingGameDbModel.gameDbModel
        return com.dauto.gamediscoveryapp.domain.entity.Game(
            id = gameDb.id,
            name = gameDb.name,
            description = gameDb.description,
            released = gameDb.released,
            backgroundImage = gameDb.backgroundImage,
            rating = gameDb.rating,
            ratingTop = gameDb.ratingTop,
            metacritic = gameDb.metacritic.toString(),
            platforms = platformsDbToEntity(pagingGameDbModel.platformList),
            genres = genresDbToEntity(pagingGameDbModel.genresList)
        )
    }

    fun gameDtoToDbModel(gameDTO: GameDTO): PagingGameDbModel {
        val game = GameDbModel(
            id = gameDTO.id,
            name = gameDTO.name,
            description = gameDTO.description,
            released = gameDTO.released,
            backgroundImage = gameDTO.backgroundImage,
            rating = gameDTO.rating,
            ratingTop = gameDTO.ratingTop,
            metacritic = gameDTO.metacritic ?: 1
        )
        return PagingGameDbModel(
            gameDbModel = game,
            genresList = genresDtoToDb(gameDTO.genres, game.id),
            platformList = platformsDtoToDb(gameDTO.platforms, game.id)
        )
    }

    private fun platformsDbToEntity(platformsDbModel: List<PlatformsDbModel>): List<ParentPlatforms> {
        val list = mutableListOf<ParentPlatforms>()
        val parentList = ParentPlatforms.values().map { it.name.lowercase() }
        platformsDbModel.forEach {
            val platformItem = it.platformName
            if (platformItem.lowercase() in parentList) {
                list.add(ParentPlatforms.valueOf(platformItem.uppercase()))
            }
        }
        return list.toList()
    }

    private fun genresDbToEntity(genresDbModel: List<GenresDbModel>) =
        genresDbModel.map { it.genres }

    private fun platformsDtoToDb(
        platformsList: List<PlatformsDto>,
        id: Int
    ): List<PlatformsDbModel> {
        val listDbModel = mutableListOf<PlatformsDbModel>()
        platformsList.forEach {
            listDbModel.add(
                PlatformsDbModel(
                    id = 0,
                    gameId = id,
                    platformName = it.platform.name
                )
            )
        }
        return listDbModel
    }

    private fun genresDtoToDb(
        genresList: List<GenresStringList>,
        id: Int
    ): List<GenresDbModel> {
        val listDbModel = mutableListOf<GenresDbModel>()
        genresList.forEach {
            listDbModel.add(
                GenresDbModel(
                    id = 0,
                    gameId = id,
                    genres = it.name
                )
            )
        }
        return listDbModel
    }

    fun listGameDtoToDbModel(listDto: List<GameDTO>): List<PagingGameDbModel> =
        listDto.map { gameDtoToDbModel(it) }

    fun listGameDTOtoEntity(listDto: List<GameDTO>): List<com.dauto.gamediscoveryapp.domain.entity.Game> =
        listDto.map { gameDtoToEntity(it) }

    fun listGameDbModelToEntity(listGameDb: List<PagingGameDbModel>) : List<com.dauto.gamediscoveryapp.domain.entity.Game> =
        listGameDb.map { gameDbModelToEntity(it) }

    fun listScreenDtoToEntity(screenshotsDto: ResultScreenshotsDto) =
        screenshotsDto.screenshotList.map {
            it.imageUrl
        }


    private fun mapGenresList(genresStringList: List<GenresStringList>): List<String> {
        val list = mutableListOf<String>()
        for (genres in genresStringList) {
            list.add(genres.name)
        }
        return list.toList()
    }

    private fun mapPlatformsList(platformsList: List<PlatformsDto>): List<ParentPlatforms> {
        val list = mutableListOf<ParentPlatforms>()
        val parentList = ParentPlatforms.values().map { it.name.lowercase() }
        platformsList.forEach {
            val platformItem = it.platform.name
            if (platformItem.lowercase() in parentList) {
                list.add(ParentPlatforms.valueOf(platformItem.uppercase()))
            }
        }

        return list.toList()
    }
}