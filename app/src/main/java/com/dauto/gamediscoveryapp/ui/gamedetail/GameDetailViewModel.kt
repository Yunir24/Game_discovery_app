package com.dauto.gamediscoveryapp.ui.gamedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dauto.gamediscoveryapp.domain.GameResult
import com.dauto.gamediscoveryapp.domain.entity.GameDetailInfo
import com.dauto.gamediscoveryapp.domain.usecase.GetGameInfoUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class GameDetailViewModel @Inject constructor(private val getUseCase: GetGameInfoUseCase) : ViewModel(){

    private val dispatcher = Dispatchers.IO

    private val _gameInfo = MutableLiveData<GameResult<GameDetailInfo>>(GameResult.Loading())
    val gameInfo: LiveData<GameResult<GameDetailInfo>> = _gameInfo

    fun getFullInfo(gameId: Int){
        viewModelScope.launch(dispatcher) {
            _gameInfo.postValue(getUseCase.getGameInfo(gameId))
        }
        getGameFromDb(gameId)
    }

    fun saveGame(gameDetailInfo: GameDetailInfo){
        viewModelScope.launch(dispatcher)  {
            getUseCase.saveGameToFavorite(gameDetailInfo)
        }
    }

    fun deleteGame(gameId: Int){
        viewModelScope.launch(dispatcher)  {
            getUseCase.deleteGameToFavorite(gameId)
        }
    }

    fun getGameFromDb(gameId:Int): LiveData<Boolean>{
        return getUseCase.getGameExist(gameId)
    }

}