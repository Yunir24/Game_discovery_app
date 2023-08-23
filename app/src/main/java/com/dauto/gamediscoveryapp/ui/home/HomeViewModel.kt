package com.dauto.gamediscoveryapp.ui.home

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dauto.gamediscoveryapp.data.GameRepositoryImpl
import com.dauto.gamediscoveryapp.domain.GameRepository
import com.dauto.gamediscoveryapp.domain.GameResult
import com.dauto.gamediscoveryapp.domain.entity.Game
import com.dauto.gamediscoveryapp.domain.usecase.GetGameList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository = GameRepositoryImpl(viewModelScope)
    private val getGameList = GetGameList(repository)
    private val liveData: LiveData<Game> = liveData {

    }
//    private val sharedFlow = MutableSharedFlow<GameResult<Game>>()
//
//
//    private val byYear = getGameList.getGameListByYear()
//        .onStart { emit(GameResult.Loading()) }
//        .mergeWith(sharedFlow)
//
//    val res = merge(byYear, sharedFlow)
//        .asLiveData()
//
//
//    fun getGameByGenres() {
//        Log.e("superTAG", "genres click")
//        viewModelScope.launch {
//            sharedFlow.emit(GameResult.Loading())
//            getGameList.getGameListByGenres().collect {
//            sharedFlow.emit(it)
//            }
//        }
//    }

    fun getGameByPlatforms(): Flow<PagingData<Game>> {

        return getGameList.getGameListByPlatforms().cachedIn(viewModelScope)


    }

    private fun <T> Flow<T>.mergeWith(flow: Flow<T>) = merge(this, flow)

    init {

//        viewModelScope.launch(Dispatchers.IO){
//            val list = getGameList.getGameListByYear()
////            for (game in list ){
////                Log.e("superTAG",game.toString())
////            }
//
//        }
    }
}