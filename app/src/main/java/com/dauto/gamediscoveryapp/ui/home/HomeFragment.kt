package com.dauto.gamediscoveryapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.dauto.gamediscoveryapp.data.GameRepositoryImpl
import com.dauto.gamediscoveryapp.databinding.FragmentHomeBinding
import com.dauto.gamediscoveryapp.domain.GameResult
import com.dauto.gamediscoveryapp.ui.GamerRecyclerView
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var adapterGame: GamerRecyclerView
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        adapterGame = GamerRecyclerView(requireContext())
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = adapterGame
        binding.recycler.layoutManager = GridLayoutManager(requireContext(), 2)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                //отменит действие флоу по идее
                homeViewModel.getGameByPlatforms().collect {
                    Log.e("SUPER", it.toString())
                    adapterGame.submitData(it)
                }
            }
        }
//        binding.getByGenres.setOnClickListener { homeViewModel.getGameByGenres() }
//        binding.getByPlatform.setOnClickListener {  }
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