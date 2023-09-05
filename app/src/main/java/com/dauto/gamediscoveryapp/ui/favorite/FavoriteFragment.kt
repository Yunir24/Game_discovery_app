package com.dauto.gamediscoveryapp.ui.favorite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.dauto.gamediscoveryapp.databinding.FragmentFavoriteBinding
import com.dauto.gamediscoveryapp.ui.ViewModelFactory
import com.dauto.gamediscoveryapp.ui.utils.getAppComponent
import javax.inject.Inject

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val component by lazy {
        getAppComponent()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val binding get() = _binding!!
    private val favoriteAdapter = FavoriteAdpater()

    private val favoriteViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[FavoriteViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = favoriteAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        favoriteAdapter.gameItemClickListener = {
            findNavController().navigate(
                FavoriteFragmentDirections.actionNavigationDashboardToGameDetailFragment(
                    it.game.id
                )
            )
        }
        favoriteViewModel.getList().observe(viewLifecycleOwner) { gameResult ->
            favoriteAdapter.submitList(gameResult)
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}