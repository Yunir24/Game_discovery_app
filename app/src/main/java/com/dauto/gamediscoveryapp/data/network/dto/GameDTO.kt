package com.dauto.gamediscoveryapp.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameDTO(
    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String ="",

    @SerialName("description")
    val description: String="",

    @SerialName("released")
    val released: String = "",

    @SerialName("background_image")
    val backgroundImage: String = "",

    @SerialName("rating")
    val rating: Double = 1.0,

    @SerialName("rating_top")
    val ratingTop: Double=1.0,

    @SerialName("parent_platforms")
    val platforms: List<PlatformsDto>,

    @SerialName("genres")
    val genres: List<GenresStringList>,
)
