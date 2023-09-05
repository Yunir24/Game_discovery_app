package com.dauto.gamediscoveryapp.domain.entity

data class Game(
    val id: Int,
    val name: String,
    val description: String,
    val released: String,
    val backgroundImage: String,
    val rating: Double,
    val ratingTop: Double,
    val platforms: List<ParentPlatforms>,
    val genres: List<String>,
)