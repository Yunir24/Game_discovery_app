package com.dauto.gamediscoveryapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dauto.gamediscoveryapp.databinding.FragmentHomeBinding
import com.dauto.gamediscoveryapp.ui.adapters.GamerRecyclerView
import com.dauto.gamediscoveryapp.ui.adapters.GamesLoadStateAdapter
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val adapterGame: GamerRecyclerView = GamerRecyclerView()
    private val homeViewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
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
        binding.recycler.adapter = adapterGame.withLoadStateFooter(
            footer = GamesLoadStateAdapter()
        )
//        binding.recycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recycler.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.filterButton.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToFilterFragment())
        }
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.getGameByPlatforms().collectLatest {
                adapterGame.submitData(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            adapterGame.addLoadStateListener {
                binding.recycler.isVisible = it.refresh !is LoadState.Loading
                binding.progressBarLoad.isVisible = it.refresh is LoadState.Loading
                binding.errorMessageTV.isVisible = it.refresh is LoadState.Error
                binding.prependProgressIndicator.isVisible = it.source.prepend is LoadState.Loading
                binding.appendProgressIndicator.isVisible = it.source.append is LoadState.Loading
            }
        }
        adapterGame.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW
        adapterGame.gameItemClickListener = {
            findNavController().navigate(
                HomeFragmentDirections.actionNavigationHomeToGameDetailFragment(it.id)
            )
        }
//        homeViewModel.res.observe(viewLifecycleOwner) {
//            when (it) {
//                is GameResult.ApiError -> {
//                    binding.textHome.text = it.status
//                    binding.progressBar.visibility=View.INVISIBLE
//                    binding.textHome.visibility=View.VISIBLE
//                    Log.e(GameRepositoryImpl.TAG, "ApiErorr!!!!")
//                }
//                is GameResult.Exception ->  {
//                    binding.textHome.text = it.toString()
//                    binding.progressBar.visibility=View.INVISIBLE
//                    binding.textHome.visibility=View.VISIBLE }
//                is GameResult.Loading -> {
//                    binding.progressBar.visibility = View.VISIBLE
//                    binding.textHome.visibility = View.INVISIBLE
//                }
//                is GameResult.Success -> {
//
//                    binding.progressBar.visibility = View.INVISIBLE
//                    binding.textHome.visibility = View.VISIBLE
//                    adapterGame.submitList(it.data)
//                }
//            }
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}