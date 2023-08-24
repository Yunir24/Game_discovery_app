package com.dauto.gamediscoveryapp.ui.gamedetail

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.dauto.gamediscoveryapp.R
import com.dauto.gamediscoveryapp.databinding.FragmentGameDetailBinding
import com.dauto.gamediscoveryapp.domain.entity.Game
import com.dauto.gamediscoveryapp.domain.entity.GameDetailInfo
import com.dauto.gamediscoveryapp.ui.adapters.GameSeriesRecyclerViewAdapter
import com.dauto.gamediscoveryapp.ui.adapters.GamerRecyclerView
import com.dauto.gamediscoveryapp.ui.adapters.PhotoAdapter
import com.dauto.gamediscoveryapp.ui.home.HomeFragmentDirections
import com.google.android.material.carousel.CarouselLayoutManager


class GameDetailFragment : Fragment() {

    private var _binding: FragmentGameDetailBinding? = null
    private val binding: FragmentGameDetailBinding
        get() = _binding ?: throw RuntimeException("FragmentGameDetailBinding is not exist")
    private val photoAAdapter = PhotoAdapter()
    private val gameSeriesAdapter = GameSeriesRecyclerViewAdapter()
    private val gameDetailViewModel by lazy {
        ViewModelProvider(this)[GameDetailViewModel::class.java]
    }
    private val args by navArgs<GameDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameDetailBinding.inflate(inflater, container, false)
        gameDetailViewModel.getFullInfo(args.gameId)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameDetailViewModel.gameInfo.observe(viewLifecycleOwner) {

            initViews(it)


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupSeriesAdapters(gameList: List<Game>) {
        binding.gameSeriesRcView.adapter = gameSeriesAdapter
        binding.gameSeriesRcView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        gameSeriesAdapter.submitList(gameList)
        gameSeriesAdapter.gameItemClickListener = {
            findNavController().navigate(
                GameDetailFragmentDirections.actionGameDetailFragmentSelf(it.id)
            )
        }

    }

    private fun initViews(gameDetailInfo: GameDetailInfo) {
        with(binding) {
            carouselRecyclerView.adapter = photoAAdapter
            carouselRecyclerView.layoutManager = CarouselLayoutManager()
            val screenList = mutableListOf(gameDetailInfo.game.backgroundImage)
            screenList.addAll(gameDetailInfo.screenshotsList)
            photoAAdapter.addList(screenList.toList())
            gameNameTextView.text = gameDetailInfo.game.name
            gameReleasedTextView.text = gameDetailInfo.game.released
            val genres = gameDetailInfo.game.genres
            if (genres.isEmpty()) {
                binding.flowHelper.isVisible = false
            } else {
                binding.flowHelper.isVisible = true
                genres.forEach {
                    val dynamicTextview = TextView(context).apply {
                        id = View.generateViewId()
                    }
                    dynamicTextview.text = it
                    dynamicTextview.setBackgroundResource(R.drawable.text_view_drawable)
                    binding.flowContainer.addView(dynamicTextview)
                    binding.flowHelper.addView(dynamicTextview)
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                gameDescriptionTextView.text = gameDetailInfo.game.description.htmlParse()
            } else {
                gameDescriptionTextView.text = gameDetailInfo.game.description
            }
            val sers = gameDetailInfo.gameSeries
            setupSeriesAdapters(sers)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
fun String.htmlParse() =
    Html.fromHtml(this, HtmlCompat.FROM_HTML_MODE_COMPACT).toString()
