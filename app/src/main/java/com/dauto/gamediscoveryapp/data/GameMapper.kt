package com.dauto.gamediscoveryapp.data

import com.dauto.gamediscoveryapp.data.network.dto.GameDTO
import com.dauto.gamediscoveryapp.data.network.dto.GenresStringList
import com.dauto.gamediscoveryapp.data.network.dto.PlatformsDto
import com.dauto.gamediscoveryapp.domain.entity.Game

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





    private fun mapGenresList(genresStringList: List<GenresStringList>): List<String> {
        val list = mutableListOf<String>()
        for (genres in genresStringList) {
            list.add(genres.name)
        }
        return list.toList()
    }

    private fun mapPlatformsList(platformsList: List<PlatformsDto>): List<String> {
        val list = mutableListOf<String>()
        for (dto in platformsList) {
            list.add(dto.platform.name)
        }
        return list.toList()
    }
}