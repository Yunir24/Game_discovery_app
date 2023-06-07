package com.dauto.gamediscoveryapp.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlatfromNameDto (
    @SerialName("name")
    val name: String
        )