package com.dauto.gamediscoveryapp.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dauto.gamediscoveryapp.domain.entity.GameDetailInfo
import com.dauto.gamediscoveryapp.domain.usecase.GetGameList
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(
    private val getGameList: GetGameList
) : ViewModel() {

    fun getList(): LiveData<List<GameDetailInfo>> {
        return getGameList.getFavoriteGameList()
    }

}