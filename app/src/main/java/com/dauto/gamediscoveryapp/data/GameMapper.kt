package com.dauto.gamediscoveryapp.data

import com.dauto.gamediscoveryapp.data.local.dbmodel.*
import com.dauto.gamediscoveryapp.data.network.dto.GameDTO
import com.dauto.gamediscoveryapp.data.network.dto.GenresStringList
import com.dauto.gamediscoveryapp.data.network.dto.PlatformsDto
import com.dauto.gamediscoveryapp.data.network.dto.ResultScreenshotsDto
import com.dauto.gamediscoveryapp.domain.entity.Game
import com.dauto.gamediscoveryapp.domain.entity.GameDetailInfo
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
            platforms = mapPlatformsListToEntity(gameDTO.platforms),
            genres = mapGenresListToEntity(gameDTO.genres)
        )


    fun gameDbModelToEntity(favoriteFullGameInfoDbModel: FavoriteFullGameInfoDbModel): GameDetailInfo {
        val gameDb = favoriteFullGameInfoDbModel.favoriteGameDbModel
        val game = Game(
            id = gameDb.id,
            name = gameDb.name,
            description = gameDb.description,
            released = gameDb.released,
            backgroundImage = gameDb.backgroundImage,
            rating = gameDb.rating,
            ratingTop = gameDb.ratingTop,
            platforms = platformsDbToEntity(favoriteFullGameInfoDbModel.listPlatforms),
            genres = genresDbToEntity(favoriteFullGameInfoDbModel.listGenres)
        )
        val screenList = gameDbPhotoToEntity(favoriteFullGameInfoDbModel.listImageList)
        return GameDetailInfo(
            game = game,
            screenshotsList = screenList,
            gameSeries = emptyList()
        )
    }

    fun gameEntityToDbModel(gameDetailInfo: GameDetailInfo): FavoriteFullGameInfoDbModel {
        val gameDb = gameDetailInfo.game
        val game = FavoriteGameDbModel(
            id = gameDb.id,
            name = gameDb.name,
            description = gameDb.description,
            released = gameDb.released,
            backgroundImage = gameDb.backgroundImage,
            rating = gameDb.rating,
            ratingTop = gameDb.ratingTop,
        )
        val screen = gameEntityPhotoToDbModel(gameDetailInfo.screenshotsList, gameDb.id)
        return FavoriteFullGameInfoDbModel(
            favoriteGameDbModel = game,
            listGenres = genresEntityToDb(gameDb.genres, game.id),
            listPlatforms = platformsEntityToDb(gameDb.platforms, game.id),
            listImageList = screen
        )
    }


    fun listGameEntityToDbModel(listDto: List<GameDetailInfo>): List<FavoriteFullGameInfoDbModel> =
        listDto.map { gameEntityToDbModel(it) }

    fun listGameDTOtoEntity(listDto: List<GameDTO>): List<Game> =
        listDto.map { gameDtoToEntity(it) }

    fun listGameDbModelToEntity(listGameDb: List<FavoriteFullGameInfoDbModel>): List<GameDetailInfo> =
        listGameDb.map { gameDbModelToEntity(it) }


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

    private fun genresDbToEntity(genresDbModel: List<FavoriteGenresDbModel>) =
        genresDbModel.map { it.genres }

    private fun platformsEntityToDb(
        platformsList: List<ParentPlatforms>,
        id: Int
    ): List<PlatformsDbModel> {
        val listDbModel = mutableListOf<PlatformsDbModel>()
        platformsList.forEach {
            listDbModel.add(
                PlatformsDbModel(
                    gameId = id,
                    platformName = it.name
                )
            )
        }
        return listDbModel
    }

    private fun genresEntityToDb(
        genresList: List<String>,
        id: Int
    ): List<FavoriteGenresDbModel> {
        val listDbModel = mutableListOf<FavoriteGenresDbModel>()
        genresList.forEach {
            listDbModel.add(
                FavoriteGenresDbModel(
                    gameId = id,
                    genres = it
                )
            )
        }
        return listDbModel
    }


    fun listScreenDtoToEntity(screenshotsDto: ResultScreenshotsDto) =
        screenshotsDto.screenshotList.map {
            it.imageUrl
        }


    private fun mapGenresListToEntity(genresStringList: List<GenresStringList>): List<String> {
        val list = mutableListOf<String>()
        for (genres in genresStringList) {
            list.add(genres.name)
        }
        return list.toList()
    }

    private fun mapPlatformsListToEntity(platformsList: List<PlatformsDto>): List<ParentPlatforms> {
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


    private fun gameDbPhotoToEntity(list: List<FavoriteImageDbModel>) = list.map { it.imageUri }
    private fun gameEntityPhotoToDbModel(
        listPhoto: List<String>,
        id: Int
    ): List<FavoriteImageDbModel> {
        val list = mutableListOf<FavoriteImageDbModel>()
        listPhoto.forEach {
            list.add(
                FavoriteImageDbModel(
                    gameId = id,
                    imageUri = it
                )
            )
        }
        return list
    }
}