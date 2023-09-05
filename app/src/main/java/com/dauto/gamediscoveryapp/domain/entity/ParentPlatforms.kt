package com.dauto.gamediscoveryapp.domain.entity

import com.dauto.gamediscoveryapp.R

enum class ParentPlatforms(val id: Int, val imageId: Int) {
    PC(1, R.drawable.windows_svgrepo_com),
    PLAYSTATION(2, R.drawable.playstation_svgrepo_com),
    XBOX(3,R.drawable.xbox_svgrepo_com),
    IOS(4,R.drawable.ios_svgrepo_com),
    ANDROID(8,R.drawable.android_svgrepo_com),
    APPLE_MACINTOSH(5,R.drawable.macintosh_svgrepo_com),
    LINUX(6,R.drawable.linux_svgrepo_com),
    NINTENDO(7,R.drawable.nintendo_switch_svgrepo_com),
    SEGA(11,R.drawable.sega_svgrepo_com),
    WEB(14,R.drawable.web_svgrepo_com)
}