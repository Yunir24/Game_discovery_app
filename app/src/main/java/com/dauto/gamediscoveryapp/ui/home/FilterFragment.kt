package com.dauto.gamediscoveryapp.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dauto.gamediscoveryapp.R
import com.dauto.gamediscoveryapp.databinding.FragmentFilterBinding
import com.dauto.gamediscoveryapp.di.InjectingSavedStateViewModelFactory
import com.dauto.gamediscoveryapp.domain.entity.GameQuery
import com.dauto.gamediscoveryapp.domain.entity.Genres
import com.dauto.gamediscoveryapp.domain.entity.ParentPlatforms
import com.dauto.gamediscoveryapp.ui.utils.getAppComponent
import com.google.android.material.chip.Chip
import com.google.android.material.slider.RangeSlider
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


class FilterFragment : Fragment() {


    private var _binding: FragmentFilterBinding? = null
    private val binding: FragmentFilterBinding
        get() = _binding ?: throw RuntimeException("FragmentFilterBinding is not exist")
    private var isFilterAccepted = false

    @Inject
    lateinit var abst: InjectingSavedStateViewModelFactory

    //    private lateinit var homeViewModel: HomeViewModel
    private val component by lazy {
        getAppComponent()
    }
    private val factory by lazy {
        abst.create(onwer = requireActivity(), null)
    }
    private val homeViewModel by lazy {
        ViewModelProvider(requireActivity(), factory)[HomeViewModel::class.java]
    }
//
//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory

//    private val homeViewModel by lazy {
//        ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
//    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)
    }

    private val genresChipsList: MutableList<Chip> = mutableListOf()
    private val platformChipsList: MutableList<Chip> = mutableListOf()
    private var genresList: MutableSet<Genres> = mutableSetOf()
    private var platformsList: MutableSet<ParentPlatforms> = mutableSetOf()
    private var yearRange: MutableList<Float> = mutableListOf()
    private var queryString: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED){
                homeViewModel.state.map {
                    it.query
                }.collect {
                    if (!isFilterAccepted) {
                        initCollectors(it)
                        isFilterAccepted = true
                    }
                }
            }

        }
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun clearSettings() {
        binding.clearTV.setOnClickListener {
            binding.chipsGenresContainer.clearCheck()
            binding.chipsPlatformsContainer.clearCheck()
            binding.rangeSlider.setValues(1970.0F, 2025.0F)
            genresList.clear()
            platformsList.clear()
            yearRange.clear()
        }

    }


    private fun collectQueryListToString(queryList: List<String>): String {
        val stringCollector = StringBuilder()
        queryList.forEach {
            stringCollector.append(it)
            stringCollector.append(",")
        }
        stringCollector.deleteAt(stringCollector.length - 1)
        return stringCollector.toString()
    }

    private fun initRangeSliderSettings(yearRangeList: List<Float>) {
        val initialStart = if (yearRangeList.isEmpty()) 1970.0F else yearRangeList[0]
        val initialEnd = if (yearRangeList.isEmpty()) 2025.0F else yearRangeList[1]
        binding.rangeSlider.setValues(initialStart, initialEnd)
        binding.rangeSlider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {

            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                val value = slider.values
                val start = value[0]
                val end = value[1]
                if (yearRange.isNotEmpty()) {
                    yearRange[0] = start
                    yearRange[1] = end
                } else {
                    yearRange.add(start)
                    yearRange.add(end)
                }
            }

        })
    }

    private fun initGenresChipsSettings() {
        Genres.values().forEach {
            val dynamicChips = layoutInflater.inflate(
                R.layout.chip_layout,
                binding.chipsGenresContainer,
                false
            ) as Chip
            dynamicChips.apply {
                id = View.generateViewId()
                text = it.name
            }
            dynamicChips.setOnCheckedChangeListener { buttonView, isChecked ->
                val genres = Genres.valueOf(buttonView.text.toString())
                if (isChecked) {
                    genresList.add(genres)
                } else {
                    genresList.remove(genres)
                }
            }
            genresChipsList.add(dynamicChips)
            binding.chipsGenresContainer.addView(dynamicChips)
        }

    }

    private fun initPlatformsChipsSetting() {
        ParentPlatforms.values().forEach {
            val dynamicChips = layoutInflater.inflate(
                R.layout.chip_layout,
                binding.chipsPlatformsContainer,
                false
            ) as Chip
            dynamicChips.apply {
                id = View.generateViewId()
                text = it.name
            }
            dynamicChips.setOnCheckedChangeListener { buttonView, isChecked ->
                val platform = ParentPlatforms.valueOf(buttonView.text.toString())
                if (isChecked) {
                    platformsList.add(platform)
                } else {
                    platformsList.remove(platform)
                }
            }
            platformChipsList.add(dynamicChips)
            binding.chipsPlatformsContainer.addView(dynamicChips)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bindState(
            uiState = homeViewModel.state,
            uiAction = homeViewModel.accept
        )

        initGenresChipsSettings()
        initPlatformsChipsSetting()

        Log.e("hometest ", "filter frag" + homeViewModel.toString())
        Log.e("hometest ", "filter frag" + factory.toString())
        Log.e("hometest ", "filter frag" + requireActivity().toString())
        clearSettings()


    }

    private fun initCollectors(gameQuery: GameQuery) {
        genresList = gameQuery.genres.toMutableSet()
        platformsList = gameQuery.platforms.toMutableSet()
        yearRange = gameQuery.date.toMutableList()
        Log.e("hashCode ", genresList.toString())
        Log.e("hashCode ", genresList.hashCode().toString())
        Log.e("hashCode ", gameQuery.genres.toString())
        Log.e("hashCode ", gameQuery.genres.hashCode().toString())
        genresChipsList.forEach {
            it.isChecked = genresList.contains(Genres.valueOf(it.text.toString()))
        }
        platformChipsList.forEach {
            it.isChecked = platformsList.contains(ParentPlatforms.valueOf(it.text.toString()))
        }

        initRangeSliderSettings(yearRange)
        queryString = gameQuery.searchQuery
    }

    private fun FragmentFilterBinding.bindState(
        uiState: StateFlow<UIState>,
        uiAction: (UIAction) -> Unit
    ) {


        confirmButton.setOnClickListener {
            uiAction(
                UIAction.Search(
                    collectFiltersAndConfirm()
                )
            )
            findNavController().popBackStack()
//            findNavController().navigate(FilterFragmentDirections.actionFilterFragmentToNavigationHome())
        }
    }

    private fun collectFiltersAndConfirm(): GameQuery = GameQuery(
        searchQuery = queryString,
        date = yearRange,
        genres = genresList.toList(),
        platforms = platformsList.toList()
    )


    override fun onDestroy() {
        _binding = null
        Log.e("hashCode ", "on destroy")
        super.onDestroy()
    }
}