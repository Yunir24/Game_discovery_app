package com.dauto.gamediscoveryapp.data.network.dto

//import com.squareup.moshi.Json
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


    @SerialName("metacritic")
    val metacritic: Int? = 0,

    @SerialName("platforms")
    val platforms: List<PlatformsDto>,

    @SerialName("genres")
    val genres: List<GenresStringList>,
)
//(
//@field:Json(name = "id")
//val id: Int,
//
//@field:Json(name = "name")
//val name: String,
//
//@field:Json(name = "description")
//val description: String="",
//
//@field:Json(name = "released")
//val released: String,
//
//@field:Json(name = "background_image")
//val backgroundImage: String,
//
//@field:Json(name = "rating")
//val rating: Double,
//
//@field:Json(name = "rating_top")
//val rating_top: Double,
//
//@field:Json(name = "metacritic")
//val metacritic: String,
////
////    @field:Json(name = "platforms")
////    val platforms: PlatformsDto,
////    val parentPlatformDtos: List<ParentPlatformsDto>,
//val genres: List<GenresStringList>,
//)