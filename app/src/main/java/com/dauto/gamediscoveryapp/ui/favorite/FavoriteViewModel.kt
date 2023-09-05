package com.dauto.gamediscoveryapp.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dauto.gamediscoveryapp.domain.GameResult
import com.dauto.gamediscoveryapp.domain.entity.Game
import com.dauto.gamediscoveryapp.domain.usecase.GetGameList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(
    getGameList: GetGameList
) : ViewModel() {



    var _stateFlow = MutableStateFlow<GameResult<List<Game>>>(GameResult.Loading())
    var stateFlow: StateFlow<GameResult<List<Game>>> = _stateFlow


    init {
        viewModelScope.launch {
            getGameList.getFavoriteGameList().collect{
                _stateFlow.emit(it) }
        }
    }

}