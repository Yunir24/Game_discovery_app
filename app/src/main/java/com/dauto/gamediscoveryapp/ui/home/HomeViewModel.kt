package com.dauto.gamediscoveryapp.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dauto.gamediscoveryapp.domain.entity.Game
import com.dauto.gamediscoveryapp.domain.entity.GameQuery
import com.dauto.gamediscoveryapp.domain.usecase.GetGameList
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel @AssistedInject constructor(
    private val getGameList: GetGameList,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val LAST_SEARCH_QUERY_KEY = "last_querry_key"
        private const val LAST_QUERY_SCROLLED_KEY = "last_querry_scrolled_key"
    }

    val state: StateFlow<UIState>

    val pagingData: Flow<PagingData<Game>>

    val accept: (UIAction) -> Unit

    override fun onCleared() {
        super.onCleared()
        savedStateHandle[LAST_SEARCH_QUERY_KEY] = state.value.query
        savedStateHandle[LAST_QUERY_SCROLLED_KEY] = state.value.lastQuerryScrolled
    }

    private fun searchRepo(gameQuery: GameQuery): Flow<PagingData<Game>> {
        return getGameList.getGameListByPaging(gameQuery)
    }

    init {
        val initialQuery: GameQuery = savedStateHandle[LAST_SEARCH_QUERY_KEY] ?: DEFAULT_QUERY
        val lastQueryScrolled: GameQuery =
            savedStateHandle[LAST_QUERY_SCROLLED_KEY] ?: DEFAULT_QUERY

        val actionStateFlow = MutableSharedFlow<UIAction>()

        val searches = actionStateFlow
            .filterIsInstance<UIAction.Search>()
            .distinctUntilChanged()
            .onStart { emit(UIAction.Search(query = initialQuery)) }

        val queryScrolled = actionStateFlow
            .filterIsInstance<UIAction.Scroll>()
            .distinctUntilChanged()
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                replay = 1
            )
            .onStart { emit(UIAction.Scroll(currentQuery = lastQueryScrolled)) }

        pagingData = searches
            .flatMapLatest {
                searchRepo(gameQuery = it.query)
            }.cachedIn(viewModelScope)
        state = combine(
            searches,
            queryScrolled,
            ::Pair
        ).map { (search, scroll) ->
            UIState(
                query = search.query,
                lastQuerryScrolled = scroll.currentQuery,
                hasNotScrolledForCurrentState = search.query != scroll.currentQuery
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = UIState()
        )
        accept = { action ->
            viewModelScope.launch {
                actionStateFlow.emit(action) }
        }
    }


}

sealed class UIAction {
    data class Search(val query: GameQuery) : UIAction()
    data class Scroll(val currentQuery: GameQuery) : UIAction()
}

data class UIState(
    val query: GameQuery = DEFAULT_QUERY,
    val lastQuerryScrolled: GameQuery = DEFAULT_QUERY,
    val hasNotScrolledForCurrentState: Boolean = false
)

private val DEFAULT_QUERY = GameQuery(
    date = emptyList(),
    genres = emptyList(),
    platforms = emptyList(),
    searchQuery = null)



