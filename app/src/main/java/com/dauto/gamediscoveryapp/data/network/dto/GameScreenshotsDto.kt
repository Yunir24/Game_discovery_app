package com.dauto.gamediscoveryapp.data.network.dto

import kotlinx.serialization.SerialName


@kotlinx.serialization.Serializable
data class GameScreenshotsDto(
    @SerialName("image")
    val imageUrl: String)
