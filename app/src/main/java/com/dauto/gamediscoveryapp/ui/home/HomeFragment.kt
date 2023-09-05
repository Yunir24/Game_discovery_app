package com.dauto.gamediscoveryapp.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dauto.gamediscoveryapp.databinding.FragmentHomeBinding
import com.dauto.gamediscoveryapp.di.InjectingSavedStateViewModelFactory
import com.dauto.gamediscoveryapp.domain.entity.Game
import com.dauto.gamediscoveryapp.ui.adapters.GamerRecyclerView
import com.dauto.gamediscoveryapp.ui.adapters.GamesLoadStateAdapter
import com.dauto.gamediscoveryapp.ui.utils.getAppComponent
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    @Inject
    lateinit var abst: InjectingSavedStateViewModelFactory

    private val component by lazy {
        getAppComponent()
    }
    private val factory by lazy {
        abst.create(onwer = requireActivity(), null)
    }
    private val homeViewModel by lazy {
        ViewModelProvider(requireActivity(), factory)[HomeViewModel::class.java]
    }


//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory

//    private val args by navArgs<HomeFragmentArgs>()

    //    private lateinit var homeViewModel: HomeViewModel
//    private var gameQuery = GameQuery(
//        null,
//        null,
//        null,
//        null,
//    )

//    private fun parseQueryFromArgs() {
//        val year = if ((args.years).equals(" ")) null else args.years
//        val genr = if ((args.genres).equals(" ")) null else args.genres
//        val plat = if ((args.platforms).equals(" ")) null else args.platforms
//        gameQuery = gameQuery.copy(
//            date = year,
//            genres = genr,
//            platforms = plat
//        )
//    }


    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val factory = abst.create(requireActivity(), null)
//        homeViewModel = ViewModelProvider(requireActivity(), factory)[HomeViewModel::class.java]

        Log.e("hometest ", "home frag" + factory.toString())
//        parseQueryFromArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.e("hometest", "this home fragment $homeViewModel")
        Log.e("hometest", "this home fragment ${requireActivity()}")
//        Log.e("hometest", "this home fragment $viewModelFactory")
        binding.bindState(
            uiState = homeViewModel.state,
            pagingData = homeViewModel.pagingData,
            uiAction = homeViewModel.accept
        )

//        viewLifecycleOwner.lifecycleScope.launch {
//            homeViewModel.getGameByPlatforms(null).collectLatest(adapterGame::submitData)
//        }


    }


    private fun FragmentHomeBinding.bindState(
        uiState: StateFlow<UIState>,
        pagingData: Flow<PagingData<Game>>,
        uiAction: (UIAction) -> Unit
    ) {
        val adapterGame = GamerRecyclerView()
        adapterGame.gameItemClickListener = {
            findNavController().navigate(
                HomeFragmentDirections.actionNavigationHomeToGameDetailFragment(it.id)
            )
        }
        retryButtonMain.setOnClickListener {
            adapterGame.retry()
        }
        filterButton.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionNavigationHomeToFilterFragment()
            )
        }
        bindQuery(
            uiState = uiState,
            onQueryChanged = uiAction
        )
        bindList(
            gameAdapter = adapterGame,
            uiState = uiState,
            pagingData = pagingData,
            onScrollChanged = uiAction
        )
    }

    private fun FragmentHomeBinding.bindList(
        gameAdapter: GamerRecyclerView,
        uiState: StateFlow<UIState>,
        pagingData: Flow<PagingData<Game>>,
        onScrollChanged: (UIAction.Scroll) -> Unit
    ) {
        recycler.layoutManager = GridLayoutManager(requireContext(), 2)
        recycler.adapter = gameAdapter.withLoadStateFooter(
            footer = GamesLoadStateAdapter {
                gameAdapter.retry()
            }
        )
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) onScrollChanged(UIAction.Scroll(currentQuery = uiState.value.query))
            }
        })
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                gameAdapter.addLoadStateListener {
                    val recyclerViewVisibility = when (it.refresh) {
                        LoadState.Loading -> View.INVISIBLE
                        is LoadState.Error -> View.GONE
                        else -> View.VISIBLE
                    }
                    binding.recycler.visibility = recyclerViewVisibility
                    binding.progressBarLoad.isVisible = it.refresh is LoadState.Loading
                    binding.errorMessageTV.isVisible = it.refresh is LoadState.Error
                    binding.retryButtonMain.isVisible = it.refresh is LoadState.Error
                    binding.prependProgressIndicator.isVisible =
                        it.source.prepend is LoadState.Loading
                    binding.appendProgressIndicator.isVisible =
                        it.source.append is LoadState.Loading
                }
            }
        }

        val notLoading = gameAdapter.loadStateFlow
            .distinctUntilChangedBy {
                it.source.refresh
            }
            .map {
                it.source.refresh is LoadState.NotLoading
            }
        val hasNotScrolledForCurrentSearch = uiState
            .map { it.hasNotScrolledForCurrentState }
            .distinctUntilChanged()

        val shouldScrollToTop = combine(
            notLoading,
            hasNotScrolledForCurrentSearch,
            Boolean::and
        ).distinctUntilChanged()

        viewLifecycleOwner.lifecycleScope.launch {
            pagingData.collectLatest(gameAdapter::submitData)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            shouldScrollToTop.collect { shouldScroll ->
                if (shouldScroll) recycler.scrollToPosition(0)
            }
        }
    }



    private fun FragmentHomeBinding.bindQuery(
        uiState: StateFlow<UIState>,
        onQueryChanged: (UIAction.Search) -> Unit
    ) {
        fun updateQueryFromInput(
            searchString: CharSequence?
        ){
            val newQueryString = searchString?.toString()
            val gameQuery = uiState.value.query.copy(searchQuery = newQueryString)
            onQueryChanged(
                UIAction.Search(
                    query = gameQuery
                )
            )
        }
        editTextInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                editTextInput.text.trim().let {
                    if (it.isNotEmpty()) {
                        updateQueryFromInput(it)
//                        val gameQuery = uiState.value.query.copy(searchQuery = it.toString())
//                        onQueryChanged(
//                            UIAction.Search(
//                                query = gameQuery
//                            )
//                        )
                    }
                }
                true
            } else {
                false
            }

        }
        inputLayout.setEndIconOnClickListener {
            updateQueryFromInput(null)
//            val gameQuery = uiState.value.query.copy(searchQuery = null)
//            onQueryChanged(
//                UIAction.Search(
//                    query = gameQuery
//                )
//            )
        }
        editTextInput.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                editTextInput.text.trim().let {
                    if (it.isNotEmpty()) {
                        updateQueryFromInput(
                            it
                        )
//                        gameQuery = gameQuery.copy(searchQuery = it.toString())
//                        onQueryChanged(
//                            UIAction.Search(
//                                query = gameQuery
//                            )
//                        )
                    }
                }
                true
            } else {
                false
            }

        }
        viewLifecycleOwner.lifecycleScope.launch {
            uiState
                .map {
                    it.query
                }.distinctUntilChanged()
                .map {
                    it.searchQuery
                }.collect(editTextInput::setText)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}