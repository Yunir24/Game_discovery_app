package com.dauto.gamediscoveryapp.data.network.dto


import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ResultGameDto (
    @SerialName("results")
    var results: List<GameDTO>
    )