package com.dauto.gamediscoveryapp.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResultScreenshotsDto(
    @SerialName("results")
    val screenshotList: List<GameScreenshotsDto>
)
