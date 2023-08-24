package com.dauto.gamediscoveryapp.data

import com.dauto.gamediscoveryapp.data.network.dto.*
import com.dauto.gamediscoveryapp.domain.entity.Game
import com.dauto.gamediscoveryapp.domain.entity.ParentPlatforms

class GameMapper {
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

    fun listGameDTOtoEntity(listDto: List<GameDTO>):List<Game> = listDto.map{ gameDtoToEntity(it) }

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
        platformsList.forEach{
            val platformItem = it.platform.name
            if (platformItem.lowercase() in parentList)
            {
                list.add(ParentPlatforms.valueOf(platformItem.uppercase()))
            }
        }

        return list.toList()
    }
}