package com.dauto.gamediscoveryapp.ui.gamedetail

import android.app.Application
import androidx.lifecycle.*
import com.dauto.gamediscoveryapp.data.GameRepositoryImpl
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

    private val _gameExist = MutableLiveData<Boolean>()
    val gameExist: LiveData<Boolean> = _gameExist


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