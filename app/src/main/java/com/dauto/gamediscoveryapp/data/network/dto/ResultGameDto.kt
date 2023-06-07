package com.dauto.gamediscoveryapp.data.network.dto

//import com.squareup.moshi.Json
//import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ResultGameDto (
    @SerialName("results")
    var results: List<GameDTO>
    )
//@JsonClass(generateAdapter = true)
//class ResultGameDto (
//    @field:Json(name = "results")
//    var result: List<GameDTO>
//    )