package com.dauto.gamediscoveryapp.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dauto.gamediscoveryapp.domain.GameResult
import com.dauto.gamediscoveryapp.domain.entity.Game
import com.dauto.gamediscoveryapp.domain.entity.GameDetailInfo
import com.dauto.gamediscoveryapp.domain.usecase.GetGameList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(
    private val getGameList: GetGameList
) : ViewModel() {


    var _stateFlow = MutableLiveData<List<GameDetailInfo>>()
    var stateFlow: LiveData<List<GameDetailInfo>> = _stateFlow


    fun hetList(): LiveData<List<GameDetailInfo>> {
        return getGameList.getFavoriteGameList()
    }

}