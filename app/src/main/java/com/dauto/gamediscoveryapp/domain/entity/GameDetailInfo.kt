package com.dauto.gamediscoveryapp.domain.entity

data class GameDetailInfo
    (
    val game: Game,
    val screenshotsList: List<String>,
    val gameSeries: List<Game>
)