package com.dauto.gamediscoveryapp.data.network.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class PlatformsDto(
    @SerialName("platform")
    val platform: PlatfromNameDto
)