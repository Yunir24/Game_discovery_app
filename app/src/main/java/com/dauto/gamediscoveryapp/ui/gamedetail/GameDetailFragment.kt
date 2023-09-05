package com.dauto.gamediscoveryapp.ui.gamedetail

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.dauto.gamediscoveryapp.R
import com.dauto.gamediscoveryapp.databinding.FragmentGameDetailBinding
import com.dauto.gamediscoveryapp.domain.GameResult
import com.dauto.gamediscoveryapp.domain.entity.Game
import com.dauto.gamediscoveryapp.domain.entity.GameDetailInfo
import com.dauto.gamediscoveryapp.ui.ViewModelFactory
import com.dauto.gamediscoveryapp.ui.adapters.GameSeriesRecyclerViewAdapter
import com.dauto.gamediscoveryapp.ui.adapters.PhotoAdapter
import com.dauto.gamediscoveryapp.ui.utils.getAppComponent
import com.dauto.gamediscoveryapp.ui.utils.setGone
import com.dauto.gamediscoveryapp.ui.utils.setInvisible
import com.dauto.gamediscoveryapp.ui.utils.setVisible
import com.google.android.material.carousel.CarouselLayoutManager
import javax.inject.Inject


class GameDetailFragment : Fragment() {


    private val component by lazy {
        getAppComponent()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var _binding: FragmentGameDetailBinding? = null
    private val binding: FragmentGameDetailBinding
        get() = _binding ?: throw RuntimeException("FragmentGameDetailBinding is not exist")
    private val photoAAdapter = PhotoAdapter()
    private val gameSeriesAdapter = GameSeriesRecyclerViewAdapter()
    private val gameDetailViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[GameDetailViewModel::class.java]
    }
    private val args by navArgs<GameDetailFragmentArgs>()
    private var isViewInitial = false

    private val listTextView = mutableListOf<TextView>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)
    }

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
        gameDetailViewModel.gameInfo.observe(viewLifecycleOwner) { gameResult ->
            when (gameResult) {
                is GameResult.ApiError -> showWarning(gameResult.status)
                is GameResult.Exception -> showWarning(gameResult.message)
                is GameResult.Loading -> showProgressBar()
                is GameResult.Success -> {
                    hideWarning()
                    hideProgressBar()
                    initViews(gameResult.data)
                }
            }
        }
    }

    private fun showProgressBar() {
        with(binding) {
            progressBar.setVisible()
            gameNameTextView.setInvisible()
            favoriteButton.setInvisible()
            gameReleasedTextView.setInvisible()
            gameReleasedTitle.setInvisible()
            gameDescriptionTextView.setInvisible()
            gameDescriptionTitle.setInvisible()
            gameSeriesTitle.setInvisible()
            dividerGameSeries.setInvisible()
        }
    }

    private fun hideProgressBar() {
        with(binding) {
            progressBar.setInvisible()
            gameNameTextView.setVisible()
            favoriteButton.setVisible()
            gameReleasedTextView.setVisible()
            gameReleasedTitle.setVisible()
            gameDescriptionTextView.setVisible()
            gameDescriptionTitle.setVisible()
            gameSeriesTitle.setVisible()
            dividerGameSeries.setVisible()
        }
    }

    private fun showWarning(message: String) {
        with(binding) {
            errorMessage.setVisible()
            errorMessage.text = message
        }
    }

    private fun hideWarning() {
        binding.errorMessage.setGone()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupSeriesAdapters(gameList: List<Game>) {
        if (gameList.isEmpty()) {
            with(binding){
                dividerGameSeries.setInvisible()
                gameSeriesTitle.setInvisible()
                gameSeriesRcView.setInvisible()
            }
        } else {
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
    }

    private fun getImage(game: GameDetailInfo) {
        gameDetailViewModel.getGameFromDb(game.game.id).observe(viewLifecycleOwner){exist->
            if (exist){
                val favoriteOn =
                    ContextCompat.getDrawable(requireContext(), R.drawable.unfavorite_40px)
                with(binding.favoriteButton){
                    setImageDrawable(favoriteOn)
                    setOnClickListener {
                        gameDetailViewModel.deleteGame(game.game.id)
                    }
                }
            }else{
                val favoriteOff =
                    ContextCompat.getDrawable(requireContext(), R.drawable.favorite_40px)
                with(binding.favoriteButton){
                    setImageDrawable(favoriteOff)
                    setOnClickListener {
                        gameDetailViewModel.saveGame(game)
                    }
                }
            }
        }
    }

    private fun initViews(gameDetailInfo: GameDetailInfo) {
        with(binding) {
            gameNameTextView.text = gameDetailInfo.game.name
            gameReleasedTextView.text = gameDetailInfo.game.released
            val genres = gameDetailInfo.game.genres
            setupScreenListAdapter(gameDetailInfo)
            dynamicAddGenresIntoFlow(genres)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                gameDescriptionTextView.text = gameDetailInfo.game.description.htmlParse()
            } else {
                gameDescriptionTextView.text = gameDetailInfo.game.description
            }
            isViewInitial = true
            getImage(gameDetailInfo)
            val sers = gameDetailInfo.gameSeries
            setupSeriesAdapters(sers)
        }
    }

    private fun setupScreenListAdapter(gameDetailInfo: GameDetailInfo) {
        val screenList = mutableListOf(gameDetailInfo.game.backgroundImage)
        screenList.addAll(gameDetailInfo.screenshotsList)
        with(binding) {
            carouselRecyclerView.adapter = photoAAdapter
            carouselRecyclerView.layoutManager = CarouselLayoutManager()
            if (!isViewInitial) photoAAdapter.addList(screenList.toList())
            photoAAdapter.photoItemClickListener = {
                findNavController().navigate(
                    GameDetailFragmentDirections.actionGameDetailFragmentToFullPhotoViewerFragment(
                        screenList.toTypedArray(), it
                    )
                )
            }
        }
    }

    private fun dynamicAddGenresIntoFlow(genresList: List<String>) {
        if (genresList.isEmpty()) {
            binding.flowHelper.setInvisible()
        } else {
            binding.flowHelper.setVisible()
            if (isViewInitial) {
                listTextView.forEach {
                    binding.flowContainer.removeView(it)
                    binding.flowHelper.removeView(it)
                }
                listTextView.clear()
            } // вынужденный костыль
            genresList.forEach { genres ->
                val dynamicTextview = TextView(context).apply {
                    id = View.generateViewId()
                    text = genres
                    setBackgroundResource(R.drawable.text_view_drawable)
                }
                listTextView.add(dynamicTextview)

            }
            listTextView.forEach {
                binding.flowContainer.addView(it)
                binding.flowHelper.addView(it)
            }

        }

    }
}

@RequiresApi(Build.VERSION_CODES.N)
fun String.htmlParse() =
    Html.fromHtml(this, HtmlCompat.FROM_HTML_MODE_COMPACT).toString()
/*viewModel.getFavouriteMovie(movie.getId()).observe(this, movieFromDB -> {
            if(movieFromDB == null){
                    starImage.setImageDrawable(starOff);
                    starImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            viewModel.insertMovie(movie);
                        }
                    });
            } else{
                starImage.setImageDrawable(starOn);
                starImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewModel.removeMovie(movie.getId());
                    }
                });

            }
        });*/