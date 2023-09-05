package com.dauto.gamediscoveryapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameQuery(
    val date: List<Float>,
    val genres: List<Genres>,
    val platforms: List<ParentPlatforms>,
    val searchQuery: String?
) : Parcelable