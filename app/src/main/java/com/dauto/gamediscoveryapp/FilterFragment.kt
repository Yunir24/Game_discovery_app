package com.dauto.gamediscoveryapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dauto.gamediscoveryapp.databinding.FragmentFilterBinding
import com.dauto.gamediscoveryapp.domain.entity.Genres
import com.dauto.gamediscoveryapp.domain.entity.ParentPlatforms
import com.google.android.material.chip.Chip


class FilterFragment : Fragment() {

    private var _binding: FragmentFilterBinding? = null
    private val binding: FragmentFilterBinding
        get() = _binding ?: throw RuntimeException("FragmentFilterBinding is not exist")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Genres.values().forEach {
            val dynamicChips = layoutInflater.inflate(R.layout.chip_layout,binding.chipsGenresContainer,false) as Chip
            dynamicChips.apply {
                id = View.generateViewId()
                text = it.name

            }
            dynamicChips.setOnCheckedChangeListener { buttonView, isChecked ->
                Log.e("chipsTest", "isChecked: $isChecked")
                Log.e("chipsTest", "buttonview: ${buttonView.text}")
            }
            binding.chipsGenresContainer.addView(dynamicChips)
        }
        ParentPlatforms.values().forEach {
            val dynamicChips = layoutInflater.inflate(R.layout.chip_layout,binding.chipsPlatformsContainer,false) as Chip
            dynamicChips.apply {
                id = View.generateViewId()
                text = it.name

            }
            dynamicChips.setOnCheckedChangeListener { buttonView, isChecked ->
                Log.e("chipsTest", "isChecked: $isChecked")
                Log.e("chipsTest", "buttonview: ${buttonView.text}")
            }
            binding.chipsPlatformsContainer.addView(dynamicChips)
        }
    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}