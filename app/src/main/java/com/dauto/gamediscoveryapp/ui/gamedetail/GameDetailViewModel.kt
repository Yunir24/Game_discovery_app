package com.dauto.gamediscoveryapp.ui.gamedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dauto.gamediscoveryapp.data.GameRepositoryImpl
import com.dauto.gamediscoveryapp.domain.GameRepository
import com.dauto.gamediscoveryapp.domain.GameResult
import com.dauto.gamediscoveryapp.domain.entity.GameDetailInfo
import com.dauto.gamediscoveryapp.domain.usecase.GetGameInfoUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameDetailViewModel : ViewModel(){

    private val repository = GameRepositoryImpl(viewModelScope)
    private val getUseCase = GetGameInfoUseCase(repository)

    private val _gameInfo = MutableLiveData<GameDetailInfo>( )
    val gameInfo: LiveData<GameDetailInfo> = _gameInfo


    fun getFullInfo(gameId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _gameInfo.postValue(getUseCase.getGameInfo(gameId))
        }
    }

}